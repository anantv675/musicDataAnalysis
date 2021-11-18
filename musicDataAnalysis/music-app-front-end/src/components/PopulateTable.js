import React, {useState,useRef} from 'react'
import styled from 'styled-components'
import { useTable, useFilters, useGlobalFilter, useAsyncDebounce,usePagination } from 'react-table'
import {matchSorter} from 'match-sorter'
import makeData from '../services/makeData'

let serverData = []

const Styles = styled.div`
  padding: 1rem;

  table {
    border-spacing: 0;
    border: 1px solid black;

    tr {
      :last-child {
        td {
          border-bottom: 0;
        }
      }
    }

    th,
    td {
      margin: 0;
      padding: 0.5rem;
      border-bottom: 1px solid black;
      border-right: 1px solid black;

      :last-child {
        border-right: 0;
      }
    }
  }

  .pagination {
    padding: 0.5rem;
  }
`;

// Define a default UI for filtering
    function GlobalFilter({
                              preGlobalFilteredRows,
                              globalFilter,
                              setGlobalFilter,
                          }) {
        const count = preGlobalFilteredRows.length
        const [value, setValue] = React.useState(globalFilter)
        const onChange = useAsyncDebounce(value => {
            setGlobalFilter(value || undefined)
        }, 200)

        return (
            <span>
      Search:{' '}
                <input
                    value={value || ""}
                    onChange={e => {
                        setValue(e.target.value);
                        onChange(e.target.value);
                    }}
                    placeholder={`${count} records...`}
                    style={{
                        fontSize: '1.1rem',
                        border: '0',
                    }}
                />
    </span>
        )
    }

// Define a default UI for filtering
    function DefaultColumnFilter({
                                     column: {filterValue, preFilteredRows, setFilter},
                                 }) {
        const count = preFilteredRows.length

        return (
            <input
                value={filterValue || ''}
                onChange={e => {
                    setFilter(e.target.value || undefined) // Set undefined to remove the filter entirely
                }}
                placeholder={`Search ${count} records...`}
            />
        )
    }

// This is a custom filter UI for selecting
// a unique option from a list
    function SelectColumnFilter({
                                    column: {filterValue, setFilter, preFilteredRows, id},
                                }) {
        // Calculate the options for filtering
        // using the preFilteredRows
        const options = React.useMemo(() => {
            const options = new Set()
            preFilteredRows.forEach(row => {
                options.add(row.values[id])
            })
            return [...options.values()]
        }, [id, preFilteredRows])

        // Render a multi-select box
        return (
            <select
                value={filterValue}
                onChange={e => {
                    setFilter(e.target.value || undefined)
                }}
            >
                <option value="">All</option>
                {options.map((option, i) => (
                    <option key={i} value={option}>
                        {option}
                    </option>
                ))}
            </select>
        )
    }

    function fuzzyTextFilterFn(rows, id, filterValue) {
        return matchSorter(rows, filterValue, {keys: [row => row.values[id]]})
    }

    fuzzyTextFilterFn.autoRemove = val => !val


    function Table({
                       columns,
                       data,
                       fetchData,
                       loading,
                       pageCount: controlledPageCount,
                   }) {
        const filterTypes = React.useMemo(
            () => ({
                // Add a new fuzzyTextFilterFn filter type.
                fuzzyText: fuzzyTextFilterFn,
                // Or, override the default text filter to use
                // "startWith"
                text: (rows, id, filterValue) => {
                    return rows.filter(row => {
                        const rowValue = row.values[id]
                        return rowValue !== undefined
                            ? String(rowValue)
                                .toLowerCase()
                                .startsWith(String(filterValue).toLowerCase())
                            : true
                    })
                },
            }),
            []
        );

        const defaultColumn = React.useMemo(
            () => ({
                // Let's set up our default Filter UI
                Filter: DefaultColumnFilter,
            }),
            []
        );

        const {
            getTableProps,
            getTableBodyProps,
            headerGroups,
            prepareRow,
            page,
            canPreviousPage,
            canNextPage,
            pageOptions,
            pageCount,
            gotoPage,
            nextPage,
            previousPage,
            setPageSize,
            state,
            // Get the state from the instance
            state: {pageIndex, pageSize},
            visibleColumns,
            preGlobalFilteredRows,
            setGlobalFilter
        } = useTable(
            {
                columns,
                data,
                defaultColumn, // Be sure to pass the defaultColumn option
                filterTypes,
                initialState: {pageIndex: 0}, // Pass our hoisted table state
                autoResetPage: false,
                manualPagination: true, // Tell the usePagination
                // hook that we'll handle our own data fetching
                // This means we'll also have to provide our own
                // pageCount.
                pageCount: controlledPageCount,
            },
            useFilters, // useFilters!
            useGlobalFilter,// useGlobalFilter!
            usePagination
        )

        // Listen for changes in pagination and use the state to fetch our new data
        React.useEffect(() => {fetchData({pageIndex, pageSize})}, [fetchData, pageIndex, pageSize])
        // serverData = React.useMemo(() => props.brandData, [props.brandData]);
        // Render the UI for your table
        return (
            <>
      <pre>
        <code>
          {JSON.stringify(
              {
                  pageIndex,
                  pageSize,
                  pageCount,
                  canNextPage,
                  canPreviousPage,
              },
              null,
              2
          )}
        </code>
      </pre>
                <table {...getTableProps()}>
                    <thead>
                    {headerGroups.map(headerGroup => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps()}>
                                    {column.render('Header')}
                                    <span>
                    {column.isSorted
                        ? column.isSortedDesc
                            ? ' 🔽'
                            : ' 🔼'
                        : ''}
                  </span>
                                    <div>{column.canFilter ? column.render('Filter') : null}</div>
                                </th>
                            ))}
                        </tr>
                    ))}
                    <tr>
                        <th
                            colSpan={visibleColumns.length}
                            style={{
                                textAlign: 'left',
                            }}
                        >
                            <GlobalFilter
                                preGlobalFilteredRows={preGlobalFilteredRows}
                                globalFilter={state.globalFilter}
                                setGlobalFilter={setGlobalFilter}
                            />
                        </th>
                    </tr>
                    </thead>
                    <tbody {...getTableBodyProps()}>
                    {page.map((row, i) => {
                        prepareRow(row)
                        return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map(cell => {
                                    return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                })}
                            </tr>
                        )
                    })}
                    <tr>
                        {loading ? (
                            // Use our custom loading state to show a loading indicator
                            <td colSpan="10000">Loading...</td>
                        ) : (
                            <td colSpan="10000">
                                Showing {page.length} of ~{controlledPageCount * pageSize}{' '}
                                results
                            </td>
                        )}
                    </tr>
                    </tbody>
                </table>
                {/*
        Pagination can be built however you'd like.
        This is just a very basic UI implementation:
      */}
                <div className="pagination">
                    <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
                        {'<<'}
                    </button>
                    {' '}
                    <button onClick={() => previousPage()} disabled={!canPreviousPage}>
                        {'<'}
                    </button>
                    {' '}
                    <button onClick={() => nextPage()} disabled={!canNextPage}>
                        {'>'}
                    </button>
                    {' '}
                    <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
                        {'>>'}
                    </button>
                    {' '}
                    <span>
          Page{' '}
                        <strong>
            {pageIndex + 1} of {pageOptions.length}
          </strong>{' '}
        </span>
                    <span>
          | Go to page:{' '}
                        <input
                            type="number"
                            defaultValue={pageIndex + 1}
                            onChange={e => {
                                const page = e.target.value ? Number(e.target.value) - 1 : 0
                                gotoPage(page)
                            }}
                            style={{width: '100px'}}
                        />
        </span>{' '}
                    <select
                        value={pageSize}
                        onChange={e => {
                            setPageSize(Number(e.target.value))
                        }}
                    >
                        {[10, 20, 30, 40, 50].map(pageSize => (
                            <option key={pageSize} value={pageSize}>
                                Show {pageSize}
                            </option>
                        ))}
                    </select>
                </div>
            </>
        )
    }



    function ItemTable(props) {
        const columns = React.useMemo(
            () => [
                {
                    Header: 'Item details',
                    columns: [
                        {
                            Header: 'Date',
                            accessor: 'date',
                            disableFilters: true
                        },
                        {
                            Header: 'Title',
                            accessor: 'title',
                            disableFilters: false,
                            // filter: 'fuzzyText',
                        },
                        {
                            Header: 'Main category',
                            accessor: 'main_cat',
                            disableFilters: false,
                            Filter: SelectColumnFilter,
                            // filter: 'includes',
                        },
                        {
                            Header: 'Brand',
                            accessor: 'brand',
                            disableFilters: true
                        },
                        {
                            Header: 'Item ID',
                            accessor: 'asin',
                            disableFilters: false
                            // filter: 'fuzzyText',
                        }
                    ],
                },
            ],
            []
        );

        // serverData = React.useMemo(() => props.brandData, [props.brandData]);
        // console.log(serverData)
        const [data, setData] = React.useState([])
        const [loading, setLoading] = React.useState(false)
        const [pageCount, setPageCount] = React.useState(0)
        const fetchIdRef = React.useRef(0)
        serverData = React.useMemo(() => props.brandData, [props.brandData]);
        const fetchData = React.useCallback(({ pageSize, pageIndex }) => {
            // console.log("calling fetchData");
            // This will get called when the table needs new data
            // You could fetch your data from literally anywhere,
            // even a server. But for this example, we'll just fake it.

            // Give this fetch an ID
            const fetchId = ++fetchIdRef.current;

            // Set the loading state
            setLoading(true);
            console.log("fetchId: "+fetchId , "fetchIdRef: "+fetchIdRef.current)
            // We'll even set a delay to simulate a server here
            setTimeout(() => {
                // Only update the data if this is the latest fetch
                if (fetchId === fetchIdRef.current) {
                    console.log("Inside setTimeout")
                    console.log("fetchId: "+fetchId , "fetchIdRef: "+fetchIdRef.current)
                    const startRow = pageSize * pageIndex
                    const endRow = startRow + pageSize
                    setData(serverData.slice(startRow, endRow))

                    // Your server could send back total page count.
                    // For now we'll just fake it, too
                    setPageCount(Math.ceil(serverData.length / pageSize))

                    setLoading(false)
                }
            }, 1000)
        }, []);

        return (
            <Styles>
                <Table
                    columns={columns}
                    data={data}
                    fetchData={fetchData}
                    loading={loading}
                    pageCount={pageCount}
                />
            </Styles>
        )
    }
export default ItemTable
