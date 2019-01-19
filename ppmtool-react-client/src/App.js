import React, { Component } from 'react';
import './App.css';
import Dashboard from './component/Dashboard';
import Header from './component/Layout/Header';

class App extends Component {
  render() {
    return (
      <div className="App">
        <Header />
        <Dashboard />
      </div>
    );
  }
}

export default App;
