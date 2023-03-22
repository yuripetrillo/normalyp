import React, { Component } from 'react';
import EmployeeService from '../services/EmployeeService';
import RedirectButton from "./redirectButton";
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import { isEmail } from "validator";
import CheckButton from "react-validation/build/button";


const required = value => {
    if (!value) {
      return (
        <div className="row align-items-center justify-content-center">
          <div className="alert alert-danger" role="alert">
            This field is required!
          </div>
        </div>
      );
    }
  };
  
  const vemail = value => {
    if (!isEmail(value)) {
      return (
        <div className="alert alert-danger" role="alert">
          This is not a valid email.
        </div>
      );
    }
  };
  
  const vusername = value => {
    if (value.length < 3 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The username must be between 3 and 20 characters.
        </div>
      );
    }
  };
  
  const vname = value => {
    if (value.length < 3 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The username must be between 3 and 20 characters.
        </div>
      );
    }
  };

  const vsurname = value => {
    if (value.length < 3 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The username must be between 3 and 20 characters.
        </div>
      );
    }
  };

  export default class EmployeeComponent extends Component {
    constructor(props) {
        super(props);
    
        var url = window.location.pathname;
        var id = url.substring(url.lastIndexOf('/') + 1);
        this.state = {
            id: id,
            username: '',
            name: '',
            surname: '',
            email: ''
          
        }
        this.changeUsernameHandler = this.changeUsernameHandler.bind(this);
        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeSurNameHandler = this.changeSurNameHandler.bind(this);
        this.changeEmailHandler = this.changeEmailHandler.bind(this);
        this.saveOrEditEmployee = this.saveOrEditEmployee.bind(this);
    }

    componentDidMount(){
      if(this.state.id === '_add') {
        return
      }
      else {
      EmployeeService.getEmployeeById(this.state.id).then(response => {
          let employee = response.data;
          this.setState({username: employee.username, name: employee.name, surname: employee.surname, email: employee.email})
      });
    }
    }

    saveOrEditEmployee = (e) => {
        e.preventDefault();
        this.form.validateAll();        
        if (this.checkBtn.context._errors.length === 0) {
            let employee = {username: this.state.username, name: this.state.name, surname: this.state.surname, email: this.state.email};
        if(this.state.id === '_add') {
          EmployeeService.addEmployees(employee).then(response => {
            //Add redirect to /admin
            this.setState({
                message: Object.values(response.data),
                successful: true
             });
            },
            error => {
              const resMessage = error.response.data;
              this.setState({
                successful: false,
                message: resMessage
              });
            }
        );
        }
        else {
          EmployeeService.updateEmployee(employee, this.state.id).then(response => {
            console.log()
            this.setState({
                message: "Employee data modified successfully",
                successful: true
             });
            },
            error => {
              const resMessage = "Cannot update employee data";
              this.setState({
                successful: false,
                message: resMessage
              });
            }
        );
        }
    }
}

    changeUsernameHandler = (event) => {
        this.setState({username: event.target.value});
    }

    changeNameHandler = (event) => {
        this.setState({name: event.target.value});
    }

    changeSurNameHandler = (event) => {
        this.setState({surname: event.target.value});
    }

    changeEmailHandler = (event) => {
        this.setState({email: event.target.value});
    }

    getTitle(){
      if(this.state.id === '_add')
        return <h2 className='text-center'>Add Employee</h2>
        else {
          return <h2 className='text-center'>Update Employee</h2>
        }
    }

    render() {
        return (
                <div className="container">
                    <div className='h-100 d-flex align-items-center justify-content-center'>
                                <Form className='newElementFormCondensed' onSubmit={this.saveOrEditEmployee} ref={c => { this.form = c; }}>
                                {this.getTitle()}
                                    <div className='form-group'>
                                        <label>Username</label>
                                        <Input placeholder='Username' name='username' className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.username} onChange={this.changeUsernameHandler} validations={[required, vusername]}/>
                                    </div>  
                                    <div className='form-group'>
                                        <label>First Name</label>
                                        <Input placeholder='First Name' name='name' className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.name} onChange={this.changeNameHandler} validations={[required, vname]}/>
                                     </div>  
                                     <div className='form-group'> 
                                        <label>Last Name</label>
                                        <Input placeholder='Last Name' name='surname' className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.surname} onChange={this.changeSurNameHandler} validations={[required, vsurname]}/>
                                     </div>
                                     <div className='form-group'> 
                                        <label>Email</label>
                                        <Input placeholder='Email' name='email' className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.email} onChange={this.changeEmailHandler} validations={[required, vemail]}/>
                                    </div>
                                    <button type='submit' className='btn btn-success'>Save</button>
                                    <RedirectButton goTo='/employees' className='btn btn-danger' text='Cancel'/>
                                    <br></br>
                                    {this.state.message && (
                                      <div className="row align-items-center justify-content-center">
                                        <div
                                          className={
                                            this.state.successful
                                              ? "alert alert-success"
                                              : "alert alert-danger"
                                          }
                                          role="alert"
                                        >
                                          {this.state.message}
                                        </div>
                                      </div>
                                    )}
                                    <CheckButton
                                        style={{ display: "none" }}
                                        ref={c => {
                                            this.checkBtn = c;
                                        }}
                                    />
                                </Form>
                            </div>
                        </div>
        );
    }
}