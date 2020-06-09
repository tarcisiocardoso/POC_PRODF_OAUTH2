import React, {useState} from 'react';
import { makeStyles, createStyles } from '@material-ui/core/styles';
import {Container, Backdrop, CircularProgress} from '@material-ui/core';
import { useCurrentUser } from "../server/UseCurrentUser";

const useStyles = makeStyles((theme) =>
  createStyles({
    root: {
      flexGrow: 1,
      "margin-top": "15px"
    },
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
    },
  
    paper: {
      padding: theme.spacing(2),
      textAlign: 'center',
      color: theme.palette.text.secondary,
    },
  }),
);

function Home() {
    let [user, loading] = useCurrentUser();
    const classes = useStyles();
    const [wait, setWait] = useState(false);

    

    function isPerfilAdm(){
        return user && user.perfis && user.perfis.find(item => item === 'fazenda');
    }

    return (
    <Container maxWidth='xl' className={classes.root}>
        { !loading &&
            user && <h2>Ola: {user.name}</h2>
        }
        {
          !user && <h2>Acesso anonimo! Faça login no item de menu ao lado (canto superior direito) ===></h2>
        }
      <Backdrop className={classes.backdrop} open={loading || wait}>
        <CircularProgress color="inherit" />
      </Backdrop>

    </Container>
    )
  }

export default Home;
