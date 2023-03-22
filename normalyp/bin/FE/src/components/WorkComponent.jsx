import React, { Component } from 'react';
import WorkService from '../services/WorkService';
import RepairService from '../services/RepairService';
import RedirectButton from "./redirectButton";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import ActualRepairWorks from './ActualRepairWorks';

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
  
  const vworkingHours = value => {
    if (value < 1 || value > 8) {
      return (
        <div className="alert alert-danger" role="alert">
          Working hours must be between 1 and 8 per day.
        </div>
      );
    }
  };

  const vnote = value => {
    if (value.length < 1 || value.length > 50) {
      return (
        <div className="alert alert-danger" role="alert">
          Note length must be between 1 and 50 chars.
        </div>
      );
    }
  };
  
  export default class WorkComponent extends Component {
    constructor(props) {
        super(props);
    
        var url = window.location.pathname;
        var id = url.substring(url.lastIndexOf('/') + 1);
        var id2 = url.substring(0, url.lastIndexOf('/'));
        var id3 = id2.substring(id2.lastIndexOf('/') + 1);
        this.state = {
            id: id,
            operation: id3,
            repair:'',
            car:'',
            content: '',
            works: [],
            workingHours: '',
            note: '',
            userId: JSON.parse(localStorage.getItem('user')).id
          
        }
        this.changeWorkingHoursHandler = this.changeWorkingHoursHandler.bind(this);
        this.changeNoteHandler = this.changeNoteHandler.bind(this);
        this.saveOrEditWork = this.saveOrEditWork.bind(this);
        this.newWorkForm = this.newWorkForm.bind(this);
        this.deleteForm = this.deleteForm.bind(this);
        this.closeRepair = this.closeRepair.bind(this);
        this.redirectBack = this.redirectBack.bind(this);
        this.resumeWorkAfterAdd = this.resumeWorkAfterAdd.bind(this);
        this.addWorkExistingRepair = this.addWorkExistingRepair.bind(this);
    }

    componentDidMount(){
      RepairService.getRepairById(this.state.id).then(response => {
        this.setState({repair: response.data, car: response.data.car})
        },
        error => {
          typeof error.response.data !== 'undefined' ? this.setState({
            content: error.response.data.message.toString()
            }) : this.setState({
            content: "Cannot retreive data from BackEnd!"
            })
        });
      if(this.state.id === '_add') {
        return
      }
      else {   
      WorkService.getWorkByRepairId(this.state.id).then(response => {
          let works = response.data;
          this.setState({works: works})
      },
      error => {
        typeof error.response.data !== 'undefined' ? this.setState({
          content: error.response.data.message.toString()
          }) : this.setState({
          content: "Cannot retreive data from BackEnd!"
          })
      });
    }
  }

    saveOrEditWork = (e) => {
        e.preventDefault();
        this.form.validateAll();   
        if (this.checkBtn.context._errors.length === 0) {
            let work = {workingHours: this.state.workingHours, note: this.state.note};
          WorkService.addWork(work, this.state.userId, this.state.id).then(response => {
            this.setState({
                message: "Work added successfully",
                successful: true
             });
             window.history.back();
             if(!this.state.repair.working)
             {
              RepairService.takeRepairById(this.state.id).then(response => {
                this.setState({
                    message: "Repair taken successfully",
                    successful: true
                });
                this.resumeWorkAfterAdd(this.state.id);
                },
                error => {
                  const resMessage = "Cannot take repair";
                  this.setState({
                    successful: false,
                    message: resMessage
                  });
                  this.redirectBack();
                }
            );
             }
            },
            error => {
              const resMessage = "Cannot add work data";
              this.setState({
                successful: false,
                message: resMessage
              });
              this.redirectBack();
            }
        );
    }
}


  closeRepair = (e) => {
    e.preventDefault();
    this.form.validateAll();      
      RepairService.closeRepairById(this.state.id).then(response => {
        this.setState({
            message: Object.values(response.data),
            successful: true
        });
        this.redirectBack();
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
        }
    );
  }

  redirectBack = () => {
    window.location.pathname = "/workBoard";
  }

  resumeWorkAfterAdd = (id) => {
    window.location.pathname = "/works/manageWork/resume/" + id;
  }

    changeWorkingHoursHandler = (event) => {
        this.setState({workingHours: event.target.value});
    }

    changeNoteHandler = (event) => {
        this.setState({note: event.target.value});
    }

    addWorkExistingRepair = (event) => {
      window.location.pathname = "/works/manageWork/" + this.state.id;
    }

    getTitle(id){
      return this.state.operation === 'close' ? 
        <h2 className='text-center'>Close this Repair</h2> : 
      this.state.operation === 'resume' ? 
        <div className='jumbotron rounded cardCustom'><h1><span className="badge badge-pill badge-dark">Resume Work on car {id}</span>   <button type="button" className="rounded-pill btn btn-success border" onClick={this.addWorkExistingRepair}>Add new Work</button></h1><br></br><span className="badge badge-danger">Expected end date of this repair: {this.state.repair.endDate}</span></div>
      : <h2 className='text-center'>New Work</h2>;
    }

    changeUsernameHandler = (event) => {
      this.setState({username: event.target.value});
  }

    newWorkForm = () => {
      return (
          <Form className='newElementFormCondensed' onSubmit={this.saveOrEditWork} ref={c => { this.form = c; }}>
              <div className='form-group'>
                  <label>Today Working Hours</label>
                  <input required placeholder='Working Hours' type="number" id="workingHours" className="form-control alert-primary" min="1" max="8" value={this.state.workingHours} onChange={this.changeWorkingHoursHandler} validations={[required, vworkingHours]}/>
              </div>  
              <div className='form-group'>
                  <label>Note</label>
                  <textarea required className={this.state.note.length < 1 || this.state.note.length > 50 ? "form-control is-invalid" : "form-control is-valid"} placeholder='Note' name='note' min="1" max="50" onChange={this.changeNoteHandler} validations={[required, vnote]} value={this.state.note} id="note" rows="3"></textarea>
                </div>  
              <button type='submit' className='btn btn-success' disabled = { this.state.note.length < 1 || this.state.note.length > 50 ? "disabled" : ""}>Save</button>
              <RedirectButton goTo='/workBoard' className='btn btn-danger' text='Cancel'/>
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
      );
    };

    resumeWorkForm = () => {
      return (
        <Container className='text-center justify-content-center align-items-center' fluid>
        <Row>
             <ActualRepairWorks works={this.state.works}></ActualRepairWorks>
        </Row>
      </Container>
      );
    };

    deleteForm = () => {
      return (
        <Form className='newElementFormCondensed' onSubmit={this.closeRepair} ref={c => { this.form = c; }}>
          <div className='h-100 d-flex align-items-center justify-content-center'>
            <div className='form-group'> 
                  <label>Are you sure?</label><br></br>
                  <button type='submit' className='btn btn-success'>Yes</button>
                  <RedirectButton goTo='/workBoard' className='btn btn-danger' text='No'/>
           </div>
          </div>
        </Form>
      );
    };

    render() {
      return(
      <div className="container text-center justify-content-center align-items-center">  
        <div className="container text-center justify-content-center align-items-center" hidden = { !this.state.content.startsWith("Error") ? "hidden" : ""}>
          <header className="jumbotron rounded cardCustomRed">
            <h3>{this.state.content}</h3>
          </header>
        </div>
        {this.getTitle(this.state.car.model + " " + this.state.car.make + " " + "["+this.state.car.plate+"]")} <br></br>
            <div className='h-100 d-flex align-items-center justify-content-center'>
            {this.state.operation === 'manageWork' ? this.newWorkForm() : this.state.operation === 'resume' ? this.resumeWorkForm() : this.deleteForm()}
            </div>
      </div>
      );
    }
}