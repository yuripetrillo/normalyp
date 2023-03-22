import React, { Component } from "react";
import RedirectButton from "./redirectButton";
import EventBus from "../common/EventBus";
import AddGifImage from "./AddGif";
import CustomTable from "./CustomTable";
import { View } from 'react-native';
import RepairService from "../services/RepairService";

export default class SupervisorBoard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      repairs: [],
      content: "",
      check: false
    };
    this.deleteRepair = this.deleteRepair.bind(this);
  }

  handleCheck = () => {
    this.setState({ check: !this.state.check });
  };

  componentDidMount() {
    RepairService.getRepairsNotDone().then(
      (response) => {
        this.setState({
          repairs: response.data,
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

  deleteRepair(id){
    this.timer();
    setTimeout(() => {
      RepairService.deleteRepairById(id).then(response => {
        this.setState({repairs: this.state.repairs.filter(repair => repair.id !== id)})
      });
    }, 2000);
  }

  render() {
    const columnsHeaders = [
      //{ dataField: "repairId", text: "Repair id", sort: true },
      { dataField: "customerCF", text: "Customer CF", sort: true },
      { dataField: "carMake", text: "Car Make", sort: true, formatter: (cell, row) => {return row.car.make}},
      { dataField: "carModel", text: "Car Model", sort: true , formatter: (cell, row) => {return row.car.model}},
      { dataField: "carPlate", text: "Car Plate", sort: true , formatter: (cell, row) => {return row.car.plate}},
      { dataField: "employeeWorking", text: "Working employee name", sort: true , formatter: (cell, row) => {return row.employee.name}},
      { dataField: "actions", text: "Actions", formatter: (cell, row) => 
      <div className='text-center justify-content-center align-items-center'>
          <RedirectButton goTo='/repairs/manageRepair' values={row.repairId} className='btn btn-primary' text='Update'/>
          <RedirectButton goTo='/repairs/viewRepair' values={row.repairId} className='btn btn-info' text='View'/>
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
            <h2 className="text-center cardCustom">Repairs List</h2>
            <div className='row text-center justify-content-center align-items-center'>
              <RedirectButton goTo='/repairs/manageRepair/_add' className='btn-outline-warning btn-lg buttonCustom rounded-circle' text='Add Repair' />
            </div>
            <br></br>
            <div className='rowTable text-center'>
            <CustomTable itemsPassed={this.state.repairs} columnsHeadersPassed={columnsHeaders} />
            {this.state.check ? (
              <div className="fadeMe">
                <div className="row h-100 justify-content-center align-items-center">
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