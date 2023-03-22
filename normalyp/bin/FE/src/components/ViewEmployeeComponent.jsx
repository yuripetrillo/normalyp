import React, { Component } from 'react';
import EmployeeService from '../services/EmployeeService';
import RedirectButton from "./redirectButton";


class ViewEmployeeComponent extends Component {
    constructor(props) {
        super(props);
    
        var url = window.location.pathname;
        var id = url.substring(url.lastIndexOf('/') + 1);
        this.state = {
            id: id,
            employee: {}
          
        }
    }

    componentDidMount(){
        EmployeeService.getEmployeeById(this.state.id).then(response => {
            this.setState({employee: response.data});
        });
    }




    render() {
        return (
            <div>
                <div className='cardCustomBlue card col-md-6 offset-md-3 text-center justify-content-center align-items-center'>
                    <h3 className='text-center'>View Employee Details</h3>
                    <div className='alert alert-primary'>
                        <div className='row text-center justify-content-center align-items-center'>
                            <span className="label label-primary shadow-none p-1 mb-2 bg-light rounded">Employee First Name: </span>
                            <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded"> { this.state.employee.name } </p>
                        </div>
                        <div className='row text-center justify-content-center align-items-center'>
                            <span className="label label-primary shadow-none p-1 mb-2 bg-light rounded">Employee Last Name: </span>
                            <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded"> { this.state.employee.surname } </p>
                        </div>
                        <div className='row text-center justify-content-center align-items-center'>
                            <span className="label label-primary shadow-none p-1 mb-2 bg-light rounded">Employee Email:  </span>
                            <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded"> { this.state.employee.email } </p>
                        </div>
                        <div className='row text-center justify-content-center align-items-center'>
                            <span className="label label-primary shadow-none p-1 mb-2 bg-light rounded">Employee Username: </span>
                            <p className="font-weight-bold shadow-none p-1 mb-2 bg-light rounded"> { this.state.employee.username } </p>
                        </div>
                        </div>
                        <RedirectButton goTo='/employees' className='btn btn-outline-dark' text='Back to Employees'/>
                    </div>
                </div>
        );
    }
}

export default ViewEmployeeComponent;