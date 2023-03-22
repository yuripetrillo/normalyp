import React, { Component } from 'react';
import CarService from '../services/CarService';
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Select from "react-select";

const required = value => {
    if (!value) {
      return (
        <div className="alert alert-danger" role="alert">
          This field is required!
        </div>
      );
    }
  };
  
  const vplate = value => {
    if (value.length < 7 || value.length > 7) {
      return (
        <div className="alert alert-danger" role="alert">
          The Plate Code must be 7 characters.
        </div>
      );
    }
  };

  export default class CarComponent extends Component {
    constructor(props) {
        super(props);
    
        var url = window.location.pathname;
        var id = url.substring(url.lastIndexOf('/') + 1);
        this.state = {
            id: id,
            plate: props.value,
            customer: props.customerCF,
            year: '',
            model: '',
            make: '',
            successfulYears: false,
            successfulMakes: false,
            successfulModels: false,
            years: [],
            makes: [],
            models: [],
            logo: '',
            visible: true         
        }
        this.changePlateHandler = this.changePlateHandler.bind(this);
        this.changeYearHandler = this.changeYearHandler.bind(this);
        this.changeMakeHandler = this.changeMakeHandler.bind(this);
        this.changeModelHandler = this.changeModelHandler.bind(this);
        this.saveOrEditCar = this.saveOrEditCar.bind(this);
    }

   componentDidMount(){
      if(this.state.id === '_add') {
        CarService.getModelYears().then(response => {
          this.setState({
              years: response.data.menuItem,
           });
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
      CarService.getCarById(this.state.id).then(response => {
          this.setState({plate: this.state.plate, model: this.state.model, make: this.state.make})
      });
      }
    }

    sendData = (e) => {
      this.props.parentCallback(e);
    }

    redirectBack = () => {
      return window.location.pathname;
    }

    saveOrEditCar = (e) => {
        e.preventDefault();
        this.form.validateAll();        
        if (this.checkBtn.context._errors.length === 0) {
            let car = {plate: this.state.plate, model: this.state.model, make: this.state.make};
        if(this.state.id === '_add') {
          CarService.addCar(car, this.state.customer).then(response => {
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
          CarService.updateCar(car, this.state.id).then(response => {
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
    }
  }

    changePlateHandler = (event) => {
        this.setState({plate: event.target.value});
    }

    changeYearHandler = (event) => {
      this.setState({year: event.target.value});
    }

    changeMakeHandler = (event) => {
      this.setState({make: event.target.value});
    }

    changeModelHandler = (event) => {
        this.setState({model: event.target.value});
    }

    getTitle(){
      if(this.state.id === '_add')
        return <h2 className='text-center'>Add Car</h2>
        else {
          return <h2 className='text-center'>Update Car</h2>
        }
    }

    onChangeDDYear = (item, name) => {
      this.setState({year: item.value, successfulYears: true});
      CarService.getMakesByYear(item.value).then(response => {
        console.log(response.data.menuItem)
        this.setState({
          makes: response.data.menuItem,
       });
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

    onChangeDDMake = (item, name) => {
      let logoName = item.text.replaceAll(' ', '')+".jpg";
      this.setState({make: item.value, successfulMakes: true, logo: logoName});
      CarService.getModelsByYearAndMake(this.state.year, item.value).then(response => {
        this.setState({
          models: response.data.menuItem,
       });
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

    onChangeDDModel = (item, name) => {
      this.setState({model: item.value, successfulModels: true});
    }

    //handleLogo = e => this.setState({ logo: e.target.value });

    render() {
        return (
                <div className="container" hidden = { !this.state.visible ? "hidden" : ""}>
                        <div className='h-100 d-flex align-items-center justify-content-center'>
                            <Form className='newElementFormCondensed' onSubmit={this.saveOrEditCar} ref={c => { this.form = c; }}>
                                {this.getTitle()}
                                <div className='h-100 d-flex align-items-center justify-content-center'>             
                                  <div className='card' hidden = { !this.state.successfulMakes ? "hidden" : ""}>
                                    {console.log("YUR"+this.state.logo)}
                                    <img src={require(`../images/AstonMartin.jpg`)} alt="" width="60px" height="60px"/>
                                  </div>
                                </div>
                                <div className='form-group'>
                                    <label>Plate</label>
                                    <Input placeholder='Plate' name='plate' disabled = "disabled" className={ this.state.id !== '_add' ? 'form-control alert-primary' : 'form-control'} value={this.state.plate} onChange={this.changePlateHandler} validations={[required, vplate]}/>
                                  </div>
                                  <div className='form-group'>
                                  <Select
                                    isDisabled = { this.state.successfulYears ? "disabled" : ""}
                                    getOptionLabel={option => option.text}
                                    getOptionValue={option => option.value}
                                    options={this.state.years}
                                    placeholder="Select Year"
                                    onChange={this.onChangeDDYear}
                                    isSearchable={true}
                                  />
                                  </div>
                                  <div className='form-group'>
                                  <Select
                                    isDisabled = { this.state.successfulMakes ? "disabled" : ""}
                                    getOptionLabel={option => option.text}
                                    getOptionValue={option => option.value}
                                    options={this.state.makes}
                                    placeholder="Select Make"
                                    onChange={this.onChangeDDMake}
                                    isSearchable={true}
                                  />
                                  </div>  
                                <div className='form-group'>
                                <Select
                                    isDisabled = { this.state.successfulModels ? "disabled" : ""}
                                    getOptionLabel={option => option.text}
                                    getOptionValue={option => option.value}
                                    options={this.state.models}
                                    placeholder="Select Model"
                                    onChange={this.onChangeDDModel}
                                    isSearchable={true}
                                  />
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