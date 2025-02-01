import {useLanguage} from "../../components/language_context/LanguageContext"
import avatar from "../login/img_avatar2.png"
import "./Register.css"

import {useState} from "react"
import axios from "axios"
import {useNavigate} from "react-router-dom"

function Register({setIsLoggedIn}) {
    const {language, changeLanguage} = useLanguage()
    const navigate = useNavigate()
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [confirmedPassword, setConfirmedPassword] = useState("")
    const [fullname, setFullname] = useState("")
    const [bio, setBio] = useState("")
    const [email, setEmail] = useState("")
    const [error, setError] = useState({
        general: "",
        passwordError: "",
    })
    const [loading, setLoading] = useState()
    const [success, setSuccess] = useState(false)

    function validateForm(input) {

        if (input === password) {
            setConfirmedPassword(confirmedPassword)
            setError(prev => ({...prev, passwordError: ""}))
            return
        } else {
            setConfirmedPassword("")
            setError(prev => ({...prev, passwordError: "Passwords don't match"}))
        }
        if (!username || !password || !input) {
            setError(prev => ({...prev, general: "Please fill all required"}))
        } else {
            setError(prev => ({...prev, general: ""}))
        }
    }

    function signup_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        changeLanguage("english")

        setError("")
        setIsLoggedIn(true)

    }

    function signup_error(errorlist) {
        setError(prev => ({...prev, general: errorlist[0]["title"]}))
    }

    function login_success(data) {
        localStorage.setItem("username", data["username"])
        localStorage.setItem("jwt", data["token"])
        localStorage.setItem("status", JSON.stringify(data["statusEntities"]))
        localStorage.setItem("priority", JSON.stringify(data["priorityEntities"]))
        localStorage.setItem("avatarUrl", data["avatarUrl"])
        changeLanguage("english")

        setError("")
        setIsLoggedIn(true)
        navigate("/")
    }

    function login_error(errorlist) {
        setError(errorlist)
    }

    function login(e) {
        e.preventDefault()
        setLoading(true)
        const login_url = "http://localhost:8000/login"
        axios
            .post(login_url, {
                username: username,
                password: password,
            })
            .then((response) => response.data)
            .then((data) =>
                data["errorlist"].length === 0
                    ? login_success(data)
                    : login_error(data["errorlist"]),
            )
            .catch((error) => setError(error))
            .finally(() => setLoading(false))
    }

    function submit(e) {
        e.preventDefault()
        const signup_url = "http://localhost:8000/registerUser"
        if (error.passwordError === "" && error.general === "") {
            setLoading(true)
            axios.post(signup_url, {
                username: username,
                password: password,
                confirmPasswod: confirmedPassword,
                fullname: fullname,
                bio: bio,
                email: email,
            })
                .then(response => response.data)
                .then(data => data["errorlist"].length === 0 ? signup_success(data) : signup_error(data["errorlist"]))
                .then(() => login(e))
                .catch(error => console.error(error))
                .finally(() => setLoading(false))
        }
    }

    return (
        <div className={"register"}>
            <form onSubmit={submit}>
                <div className={"imgcontainer"}>
                    <img src={avatar} alt="Avatar" className={"avatar"}/>
                </div>
                <div className={"container"}>
                    <label htmlFor="uname"><b>Username</b></label>
                    <input type="text" placeholder="Enter Username" name="uname" required
                           onChange={e => setUsername(e.target.value)}/>
                    <label htmlFor="fname"><b>Fullname</b></label>
                    <input type="text" placeholder="Enter Fullname" name="fname"
                           onChange={e => setFullname(e.target.value)}/>
                    <label htmlFor="email"><b>Email</b></label>
                    <input type="email" placeholder="Enter Email" name="email" required
                           onChange={e => setEmail(e.target.value)}/>
                    <label htmlFor="bio"><b>Bio</b></label>
                    <textarea placeholder="Enter user bio" name="bio"
                              onChange={e => setBio(e.target.value)}>
                </textarea>
                    <label htmlFor="psw"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="psw" required
                           onChange={e => setPassword(e.target.value)}/>
                    <span onClick="#password-field"
                          className="field-icon toggle-password"></span>
                    <label htmlFor="cpsw"><b>Confirm Password</b></label>
                    <input className={error.passwordError ? "error" : "success"} type="password"
                           placeholder="Confirm Password" name="cpsw" required
                           onChange={e => validateForm(e.target.value)}/>
                    <p className={"error"}>{error.passwordError}</p>
                    <p className={"error"}>{error.general}</p>
                    <button type="submit" disabled={loading}>
                        {loading ? "Signing up..." : "Sign Up"}
                    </button>
                </div>
            </form>
        </div>
    )
}

export default Register