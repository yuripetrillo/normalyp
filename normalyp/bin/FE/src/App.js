import React, { Component } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import "react-datepicker/dist/react-datepicker.css";
import AuthService from "./services/auth.service";
import Login from "./components/login.component";
import Register from "./components/register.component";
import Home from "./components/home.component";
import Profile from "./components/profile.component";
import BoardUser from "./components/workBoardComponent";
import SupervisorBoard from "./components/SupervisorBoard";
import BoardAdmin from "./components/board-admin.component";
import EmployeeComponent from "./components/EmployeeComponent";
import RepairComponent from "./components/RepairComponent";
import ViewEmployeeComponent from "./components/ViewEmployeeComponent";
import ViewRepairComponent from "./components/ViewRepairComponent";
import WorkComponent from "./components/WorkComponent";
import ViewWorkComponent from "./components/ViewWorkComponent";
import EventBus from "./common/EventBus";


class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showModeratorBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user,
        showModeratorBoard: user.roles.includes("ROLE_SUPERVISOR"),
        showAdminBoard: user.roles.includes("ROLE_ADMIN"),
      });
    }
    
    EventBus.on("logout", () => {
      this.logOut();
    });
  }

  componentWillUnmount() {
    EventBus.remove("logout");
  }

  logOut() {
    AuthService.logout();
    this.setState({
      showModeratorBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    });
  }

  render() {
    const { currentUser, showModeratorBoard, showAdminBoard } = this.state;

    return (
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/"} className="navbar-brand">
            Car Management System
          </Link>
          <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to={"/home"} className="nav-link">
                Home
              </Link>
            </li>

            {showModeratorBoard && (
              <li className="nav-item">
                <Link to={"/repairs"} className="nav-link">
                  Repairs Board
                </Link>
              </li>
            )}

            {showAdminBoard && (
              <li className="nav-item">
                <Link to={"/employees"} className="nav-link">
                  Employees Board
                </Link>
              </li>
            )}

            {currentUser && (
              <li className="nav-item">
                <Link to={"/workBoard"} className="nav-link">
                  My Work
                </Link>
              </li>
            )}
          </div>

          {currentUser ? (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/profile"} className="nav-link">
                  {currentUser.username}
                </Link>
              </li>
              <li className="nav-item">
                <a href="/login" className="nav-link" onClick={this.logOut}>
                  LogOut
                </a>
              </li>
            </div>
          ) : (
            <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Login
                </Link>
              </li>

              <li className="nav-item">
                <Link to={"/register"} className="nav-link">
                  Sign Up
                </Link>
              </li>
            </div>
          )}
        </nav>

        <div className="container mt-3">
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/home" element={<Home />} />
            <Route exact path="/login" element={<Login />} />
            <Route exact path="/register" element={<Register />} />
            <Route exact path="/profile" element={<Profile />} />
            <Route exact path="/workBoard" element={<BoardUser />} />
            <Route exact path="/repairs" element={<SupervisorBoard />} />
            <Route exact path="/repairs/manageRepair/:id" element={<RepairComponent />} />
            <Route exact path="/repairs/viewRepair/:id" element={<ViewRepairComponent />} />
            <Route exact path="/employees" element={<BoardAdmin />} />
            <Route exact path="/employees/manageEmployee/:id" element={<EmployeeComponent />} />
            <Route exact path="/employees/viewEmployee/:id" element={<ViewEmployeeComponent />} />
            <Route exact path="/works/manageWork/:id" element={<WorkComponent />} />
            <Route exact path="/works/manageWork/close/:id" element={<WorkComponent />} />
            <Route exact path="/works/manageWork/resume/:id" element={<WorkComponent />} />
            <Route exact path="/works/viewWork/:id" element={<ViewWorkComponent />} />
          </Routes>
        </div>
      </div>
    );
  }
}

export default App;
