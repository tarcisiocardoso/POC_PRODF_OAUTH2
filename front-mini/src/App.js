import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';
import { useHistory } from "react-router-dom";

const API_BASE_URL = 'http://localhost:8080';
const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect'

const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
const LOCAL_AUTH_URL = API_BASE_URL + '/oauth2/authorize/customClient?redirect_uri=' + OAUTH2_REDIRECT_URI;

const chooseLogin = (
  <div>
    <div>
      <ul>
        <li>Login com Google: <a href={GOOGLE_AUTH_URL}>click here</a></li>
        <li>Login com LOCAL: <a href={LOCAL_AUTH_URL}>click here</a></li>
      </ul>
    </div>
  </div>
);

function useQuery() {
  // return new URLSearchParams(useLocation().search);
  return new URLSearchParams(window.location.search);
}

function App(props) {

  let query = useQuery();
  const [user, setUser] = useState();
  let history = useHistory();

  useEffect(()=>{
    console.log('>>>APP<<<');
    console.log( query.get('token') );
    if( !localStorage.token){
      const token = query.get('token');
      if( token ){
        localStorage.setItem("token", token);
        setUser(token);
      }
    }else{
      console.log('pegando do local: ', localStorage.token);
      setUser(localStorage.token);
    }

  });
  const handleClick = (e) => {
    console.log(">>>>handleClick<<<");


    fetch("/api/info")
      .then(res => {
        console.log("retorno", res);
        return res.json()
      })
      .then(
        (result) => { console.log(result); },
        (error) => { console.log(error); }
      );
  }
  const handleClick2 = (e) => {
    console.log(">>>>handleClick<<<");

    fetch("/rest/info", {
      method: "GET",
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
        'Authorization': `Bearer ${localStorage.token}`
      }
    })
      .then(res => {
        console.log("retorno", res);
        return res.json()
      })
      .then(
        (result) => { console.log(result); },
        (error) => { console.log(error); }
      );
  }
  const handleLogout = (e)=>{
    setUser(null);
    localStorage.removeItem('token');
    console.log( window.location.origin );
    // history.push( window.location.origin );
    // window.location.replace(window.location.origin );
    window.history.pushState("", "", '/logout');

  }
  const handleClick3 = (e) => {
    console.log(">>>>"+ query.get('token')+ '<<<<');
    console.log('>>>localStorage.token: ', localStorage.token);

  }
  return (
    
      <div className="App">
        
        {!user&&chooseLogin}

        {
          user&&
            <button onClick={handleLogout}>LogOut</button>
          }
        {
          !user?<h2>Ola Anonimo</h2>:<h2>Ola {user}</h2>
        }
        <h2>Area restrita</h2>
        <button onClick={handleClick}>Buscar informações bublica</button>
        <p />
        <button onClick={handleClick2}>Buscar informações seguras</button>
        <p />
        <button onClick={handleClick3}>Print token</button>

        <p />
        <h1>==>{query.get('name')}</h1>

      </div>
    
  );
}


export default App;
