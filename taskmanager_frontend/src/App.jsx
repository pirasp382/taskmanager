import './App.css'
import {BrowserRouter, Navigate, Route, Routes} from "react-router"
import {useEffect, useState} from "react"
import Tasks from "./pages/tasks/Tasks"
import Login from "./pages/login/Login"
import Dashboard from "./pages/dashboard/Dashboard"
import Register from "./pages/register/Register"
import Settings from "./pages/settings/Settings"


const PrivateRoute = ({children, isLoggedIn}) => {
    if (isLoggedIn === null) {
        return <div>Logging in...</div>
    }
    return isLoggedIn ? children : <Navigate to={"/login"}/>
}

function App() {

    const [isLoggedIn, setIsLoggedIn] = useState(null)
    const [loading, setLoading] = useState(true)


    useEffect(() => {
        const token = localStorage.getItem("jwt")
        if (token) {
            setIsLoggedIn(true)
        } else {
            setIsLoggedIn(false)
        }
        setLoading(false)
    }, [])

    return (
        <div>
            <BrowserRouter>

                <Routes>

                    <Route path={"/"} element={
                        <PrivateRoute isLoggedIn={isLoggedIn}>
                            <Dashboard/>
                        </PrivateRoute>
                    }/>
                    <Route path={"/tasks"} element={
                        <PrivateRoute isLoggedIn={isLoggedIn}>
                            <Tasks/>
                        </PrivateRoute>
                    }/>
                    <Route path={"/settings"} element={
                        <PrivateRoute isLoggedIn={isLoggedIn}>
                            <Settings/>
                        </PrivateRoute>
                    }/>
                    <Route path={"/login"} element={<Login setIsLoggedIn={setIsLoggedIn}/>}/>
                    <Route path={"/register"} element={<Register setIsLoggedIn={setIsLoggedIn}/>}/>
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App
