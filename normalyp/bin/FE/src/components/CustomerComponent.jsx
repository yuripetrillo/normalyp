import React, { Component } from 'react';
import CustomerService from '../services/CustomerService';
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import { isEmail } from "validator";
import CheckButton from "react-validation/build/button";
import {useNavigate} from "react-router-dom"


/*const RedirectToParent = (e) => {
  const navigate = useNavigate();
  return (
    console.log(e)
  )
};*/


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
  
  const vcf = value => {
    if (value.length < 16 || value.length > 16) {
      return (
        <div className="alert alert-danger" role="alert">
          The Customer Fiscal Code must be 16 characters.
        </div>
      );
    }
  };

  const vtelNumber = value => {
    if (value.length < 5 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The telephone number must be between 5 and 20 characters.
        </div>
      );
    }
  };
  
  const vname = value => {
    if (value.length < 3 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The surname must be between 3 and 20 characters.
        </div>
      );
    }
  };

  const vsurname = value => {
    if (value.length < 3 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          The cf must be between 3 and 20 characters.
        </div>
      );
    }
  };

  export default class CustomerComponent extends Component {
    constructor(props) {
        super(props);
    
        var url = window.location.pathname;
        var id = url.substring(url.lastIndexOf('/') + 1);
        this.state = {
            id: id,
            cf: props.value,
            telNumber : '',
            name: '',
            surname: '',
            email: '', 
            visible: true         
        }
        this.changeCFHandler = this.changeCFHandler.bind(this);
        this.changeTelNumberHandler = this.changeTelNumberHandler.bind(this);
        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changeSurNameHandler = this.changeSurNameHandler.bind(this);
        this.changeEmailHandler = this.changeEmailHandler.bind(this);
        this.saveOrEditCustomer = this.saveOrEditCustomer.bind(this);
    }

    componentDidMount(){
      if(this.state.id === '_add') {
        return
        //window.location.pathname = window.location.pathname + "/" + this.props.parent;
      }
      else {
      CustomerService.getCustomerById(this.state.id).then(response => {
          let customer = response.data;
          this.setState({cf: customer.cf, name: customer.name, surname: customer.surname, email: customer.email})
      });
    }
    }

    sendData = (e) => {
      this.props.parentCallback(e);
    }

    redirectBack = () => {
      window.location.pathname = window.location.pathname;
    }

    saveOrEditCustomer = (e) => {
        e.preventDefault();
        this.form.validateAll();        
        if (this.checkBtn.context._errors.length === 0) {
            let customer = {cf: this.state.cf, telephoneNumber: this.state.telNumber, name: this.state.name, surname: this.state.surname, email: this.state.email};
        if(this.state.id === '_add') {
          CustomerService.addCustomer(customer).then(response => {
            this.setState({
                message: Object.values(response.data),
                successful: true
             });
             this.sendData('success');
            },
            error => {
              const resMessage = error.response.data;
              this.setState({
                successful: false,
                message: resMessage
              });
              this.sendData('error');
            }
        );
        }
        else {
          CustomerService.updateCustomer(customer, this.state.id).then(response => {
            this.setState({
                message: response.data.message,
                successful: true
             });
            },
            error => {
              const resMessage =
                (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
                error.message ||
                error.toString();
              this.setState({
                successful: false,
                message: resMessage
              });
            }
        );
        }
        //console.log(this.state.message);
    }
  }

    changeCFHandler = (event) => {
        this.setState({cf: event.target.value});
    }

    changeTelNumberHandler = (event) => {
      this.setState({telNumber: event.target.value});
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
        return <h2 className='text-center'>Add Customer</h2>
        else {
          return <h2 className='text-center'>Update Customer</h2>
        }
    }

    render() {
        return (
                <div className="container" hidden = { !this.state.visible ? "hidden" : ""}>
                        <div className='h-100 d-flex align-items-center justify-content-center'>
                                <Form className='newElementForm' onSubmit={this.saveOrEditCustomer} ref={c => { this.form = c; }}>
                                  {this.getTitle()}
                                    <div className='form-group'>
                                        <label>Fiscal Code</label>
                                        <Input placeholder='Fiscal Code' name='cf' disabled = "disabled" className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.cf} onChange={this.changeCFHandler} validations={[required, vcf]}/>
                                     </div>  
                                     <div className='form-group'>
                                        <label>Telephone number</label>
                                        <Input placeholder='Telephone number' name='telNumber' className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.telNumber} onChange={this.changeTelNumberHandler} validations={[required, vtelNumber]}/>
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
                                    <button onClick={this.redirectBack} className='btn btn-danger' text='Cancel'>Cancel</button>
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