import React, { Component } from "react";
import EventBus from "../common/EventBus";
import { Navigate } from "react-router-dom";
import AuthService from "../services/auth.service";
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import RepairService from "../services/RepairService";
import RedirectButton from "./redirectButton";

export default class BoardUser extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: '',
      redirect: null,
      userReady: false,
      currentUser: { username: "" },
      logo: '',
      worksCurrent: [],
      worksToDo: []
    };
    this.getAndFormatTodayDate = this.getAndFormatTodayDate.bind(this);
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();
    if (!currentUser) this.setState({ redirect: "/home" });
    this.setState({ currentUser: currentUser, userReady: true })

    RepairService.getRepairByEmployeeId(currentUser.id).then(
      (response) => {
        this.setState({
          worksCurrent: response.data,
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

    RepairService.getRepairToDoByEmployeeId(currentUser.id).then(
      (response) => {
        this.setState({
          worksToDo: response.data,
        });
        response.data.length < 1 ? this.setState({ content: "No new works available" }) : this.setState({ content: "New works available" });
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

  getAndFormatTodayDate = () => {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0');
    var yyyy = today.getFullYear();

    today = dd + '/' + mm + '/' + yyyy;
    return <span className="badge badge-pill badge-light">{today}</span>;
  }

 
  render() {
    if (this.state.redirect) {
      return <Navigate to={this.state.redirect} />
    }
    
    //(this.state.worksCurrent.length < 1 && this.state.worksToDo.length < 1) ? this.setState({ content: "No works available" }) : this.setState({ content: "No works available" });

    const getWorkColoumns = () => {
      let items = this.state.worksCurrent.map(function(item, key){
        let logoName = item.car.make.replaceAll(' ', '')+".jpg";
        return (
          <Col key={key} className="d-flex align-items-stretch">
            <Card className='rounded cardCustomGreen'>
            <Card.Img variant="top" src={"../images/logos/"+logoName} />
            <RedirectButton goTo='/works/manageWork' values={"resume/"+item.repairId} className='btn btn-success border' text='Resume this Work'/>
            <RedirectButton goTo='/works/manageWork' values={"close/"+item.repairId} className='btn btn-danger' text='Close This'/>
            <Card.Body>
              <Card.Title>{item.repairType}</Card.Title>
              <Card.Text>
                {item.car.make}<br></br>
                {item.car.model}<br></br>
                {item.car.plate}
              </Card.Text>
            </Card.Body>
          </Card>
          </Col>
        );
      });
    return items;
    };


    const getWorkToDoColoumns = () => {
      let items = this.state.worksToDo.map(function(item, key){
        let logoName = item.car.make.replaceAll(' ', '')+".jpg";
        return (
          <Col key={key} className="d-flex align-items-stretch">
            <Card className='rounded cardCustomBlue'>
            <span className="badge badge-pill badge-success">New Work</span><br></br>
            <Card.Img variant="top" src={"../images/logos/"+logoName} />
            <RedirectButton goTo='/works/manageWork' values={item.repairId} className='btn btn-primary' text='Work on This'/>
            <Card.Body>
              <Card.Title>{item.repairType}</Card.Title>
              <Card.Text>
                {item.car.make}<br></br>
                {item.car.model}<br></br>
                {item.car.plate}
              </Card.Text>
            </Card.Body>
          </Card>
          </Col>
        );
      });
    return items;
    };

    /*<Button className='btn btn-danger' onClick={() => this.closeRepair('hello')}>Close This</Button>*/
    return (
      <div className="container text-center justify-content-center align-items-center">
      <header className={this.state.content === 'New works available' ? 'jumbotron rounded cardCustom' : 'jumbotron rounded cardCustomRed'} >
        <span className={this.state.content === 'New works available' ? 'badge badge-pill badge-success' : 'badge badge-pill badge-danger'}><h5> {this.getAndFormatTodayDate()} {this.state.content}</h5></span>
      </header>
      <Container className='text-center justify-content-center align-items-center' fluid>
        <Row>
             {getWorkColoumns()}
             {getWorkToDoColoumns()}
        </Row>
      </Container>  
      </div>
    );
  }
}