import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "react-bootstrap-table-next/dist/react-bootstrap-table2.css";
import "react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";

const CustomTable = ({itemsPassed, columnsHeadersPassed}) => {
  const columnsHeaders = columnsHeadersPassed;
  const defaultSorted = [
    {
      dataField: "name",
      order: "desc"
    }
  ];

  const pagination = paginationFactory({
    page: 1,
    sizePerPage: 5,
    lastPageText: ">>",
    firstPageText: "<<",
    nextPageText: ">",
    prePageText: "<",
    showTotal: true,
    alwaysShowAllBtns: true,
    onPageChange: function (page, sizePerPage) {
    },
    onSizePerPageChange: function (page, sizePerPage) {
    }
  });

  return (
    <div className="BootstrapTable">
      <BootstrapTable 
        bootstrap4
        keyField="id"
        data={itemsPassed}
        columns={columnsHeaders}
        defaultSorted={defaultSorted}
        pagination={pagination}
        striped
        hover
        condensed
        responsive
        rowStyle={ { width: '100%' } }
      />
    </div>
  );
}

export default CustomTable;
