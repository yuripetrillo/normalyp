import React, { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/auth.service";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { username: "" },
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) this.setState({ redirect: "/home" });
    this.setState({ currentUser: currentUser, userReady: true });
  }

  render() {
    if (this.state.redirect) {
      return <Navigate to={this.state.redirect} />;
    }

    const { currentUser } = this.state;
    
    return (
      <div className="container">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css"/>
        {this.state.userReady ? (
          <div className="row">
            <div className="col-12">
              <div className="my-5">
                <hr />
              </div>
              <div className="row mb-5 gx-5">
                <div className="col-xxl-6 mb-5 mb-xxl-0 border border-dark rounded">
                  <div className="bg-secondary-soft px-4 py-5 rounded">
                    <h4 className="mb-4 mt-0">
                      My detail [<strong>{currentUser.username}</strong>]
                    </h4>
                    <div className="row g-3">
                      <div className="col-md-6">
                        <label className="form-label">
                          My token <i className="bi bi-bezier me-2"></i>
                        </label>
                        <input
                          disabled
                          type="text"
                          className="form-control border border-primary"
                          placeholder="My token"
                          aria-label="My token information"
                          value={currentUser.accessToken.substring(0, 20) + "..." + currentUser.accessToken.substr(currentUser.accessToken.length - 20)}
                        />
                      </div>
                      <div className="col-md-6">
                        <label className="form-label">
                          My username <i className="bi bi-person-check-fill me-2"></i>
                        </label>
                        <input
                          disabled
                          type="text"
                          className="form-control border border-primary"
                          placeholder="My username"
                          aria-label="username"
                          value={currentUser.username}
                        />
                      </div>
                      <div className="col-md-6">
                        <label className="form-label">
                          My identification number <i className="bi bi-person-vcard-fill me-2"></i>
                        </label>
                        <input
                          disabled
                          type="text"
                          className="form-control border border-primary"
                          placeholder="My identification number"
                          aria-label="My identification number"
                          value={currentUser.id}
                        />
                      </div>
                      <div className="col-md-6">
                        <label className="form-label">
                          My roles <i className="bi bi-ui-checks me-2"></i>
                        </label>
                        <input
                          disabled
                          type="text"
                          className="form-control border border-primary"
                          placeholder="My roles"
                          aria-label="My roles"
                          value={currentUser.roles}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : null}
      </div>
    );
  }
}
