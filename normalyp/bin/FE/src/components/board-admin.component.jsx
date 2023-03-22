import React, { Component } from "react";
import RedirectButton from "./redirectButton";
import EmployeeService from '../services/EmployeeService';
import EventBus from "../common/EventBus";
import AddGifImage from "./AddGif";
import CustomTable from "./CustomTable";
import { View } from 'react-native';


export default class BoardAdmin extends Component {
  constructor(props) {
    super(props);

    this.state = {
      employees: [],
      content: "",
      check: false
    };
    this.deleteEmployee = this.deleteEmployee.bind(this);
  }

  handleCheck = () => {
    this.setState({ check: !this.state.check });
  };

  componentDidMount() {
    EmployeeService.getEmployees().then(
      (response) => {
        this.setState({
          employees: response.data,
        });
      },
      error => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString()
        });

        if (error.response && error.response.status === 401) {
          EventBus.dispatch("logout");
        }
      }
    );
  }

  timer = () => {
    setTimeout(() => {
      this.handleCheck();
    }, 2000);
    this.handleCheck();
  }

  deleteEmployee(id){
    this.timer();
    setTimeout(() => {
      EmployeeService.deleteEmployeeById(id).then(response => {
        this.setState({employees: this.state.employees.filter(employee => employee.id !== id)})
      });
    }, 2000);
  }

  render() {
    const columnsHeaders = [
      //{ dataField: "id", text: "Employee id", sort: true },
      { dataField: "username", text: "Employee Username", sort: true },
      { dataField: "name", text: "Employee First Name", sort: true },
      { dataField: "surname", text: "Employee Last Name", sort: true },
      { dataField: "email", text: "Employee Email", sort: true },
      { dataField: "actions", text: "Actions", formatter: (cell, row) => 
      <div className='text-center justify-content-center align-items-center'>
          <RedirectButton goTo='/employees/manageEmployee' values={row.id} className='btn btn-primary' text='Update'/>
          <RedirectButton goTo='/employees/viewEmployee' values={row.id} className='btn btn-info' text='View'/>
      </div>
      },
      { dataField: "delete", text: "Delete", formatter: (cell, row) => 
      <div className='text-center justify-content-center align-items-center'>
          <button onClick={() => this.deleteEmployee(row.id)} className='btn btn-danger' style={{marginLeft:"5px"}}>Delete</button>
      </div>
      }
    ];
    
    return (
        <div>
            <h2 className="text-center cardCustom">Employee List</h2>
            <div className='row text-center justify-content-center align-items-center'>
              <RedirectButton goTo='/employees/manageEmployee/_add' className='btn-outline-warning btn-lg buttonCustom rounded-circle' text='Add Employee' />
            </div>
            <br></br>
            <div className='rowTable text-center'>
            <CustomTable itemsPassed={this.state.employees} columnsHeadersPassed={columnsHeaders} />
            {this.state.check ? (
              <div className="fadeMe">
                <div className="row h-150 justify-content-center align-items-center">
                  <div id='containerDivFade' className="container d-flex justify-content-center rounded">
                  <View>
                    <AddGifImage />
                  </View>
                  </div>
                </div>
              </div>
            ) : null 
            }
            </div>
        </div>
    );
}

}