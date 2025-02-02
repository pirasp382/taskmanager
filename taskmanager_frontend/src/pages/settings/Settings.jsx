import Sidebar from "../../components/sidebar/Sidebar"
import "./Settings.css"
import {useLanguage} from "../../components/language_context/LanguageContext"
import {data, useNavigate} from "react-router-dom"
import {useEffect, useState} from "react"
import axios from "axios"

function Settings() {
    const {language, changeLanguage} = useLanguage()
    const [username, setUsername] = useState("")
    const [fullname, setFullname] = useState("")
    const [bio, setBio] = useState("")
    const [email, setEmail] = useState("")
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState({
        general: "",
        passwordError: "",
    })

    useEffect(() => {
        getUserInfos()
    }, [])

    function fillUserData(data) {
        setUsername(data.username)
        setFullname(data.fullname)
        setEmail(data.email)
        setBio(data.bio)
    }

    function getUserInfos() {
        const get_data_url = "http://localhost:8000/getUserData"
        const token = localStorage.getItem("jwt")
        axios
            .get(get_data_url, {
                headers: {
                    Authorization: `Bearer: ${token}`,
                },
            })
            .then((response) => response.data)
            .then((data) => fillUserData(data))
            .catch((error) => console.log())
    }

    function editSuccessful(data){
        localStorage.setItem("username", data.username);
        localStorage.setItem("jwt", data.token);
    }

    function editError(data){
        localStorage.setItem("jwt", data.token);
    }

    function editProfile(e) {
        e.preventDefault()
        setLoading(true)
        const edit_profile_url = "http://localhost:8000/editProfile"
        const token = localStorage.getItem("jwt")
        axios
            .put(
                edit_profile_url,
                {
                    username: username,
                    fullname: fullname,
                    email: email,
                    bio: bio,
                },
                {
                    headers: {
                        Authorization: `Bearer: ${token}`,
                    },
                },
            )
            .then((response) => response.data)
            .then((data) => data.errorlist.length ===0
            ?editSuccessful(data)
            :editError(data))
            .catch((error) => {
                console.log("error")
            })
            .finally(()=>setLoading(false))
    }

    return (
        <div className="settings">
            <Sidebar/>
            <div className="main bg-gray-100 p-8 rounded-lg shadow-lg max-w-2xl mx-auto">
                <div className="title mb-6">
                    <h2 className="text-3xl font-bold text-blue-600">Edit your Profile</h2>
                </div>
                <form onSubmit={editProfile}>
                    <div className="container grid gap-4">
                        <div>
                            <label htmlFor="uname" className="block text-gray-700 font-medium mb-1">
                                Username
                            </label>
                            <input
                                type="text"
                                placeholder="Enter Username"
                                name="uname"
                                className="w-full p-2 border rounded focus:outline-blue-500"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div>
                            <label htmlFor="fname" className="block text-gray-700 font-medium mb-1">
                                Fullname
                            </label>
                            <input
                                type="text"
                                placeholder="Enter Fullname"
                                name="fname"
                                className="w-full p-2 border rounded focus:outline-blue-500"
                                value={fullname}
                                onChange={(e) => setFullname(e.target.value)}
                            />
                        </div>
                        <div>
                            <label htmlFor="email" className="block text-gray-700 font-medium mb-1">
                                Email
                            </label>
                            <input
                                type="email"
                                placeholder="Enter Email"
                                name="email"
                                className="w-full p-2 border rounded focus:outline-blue-500"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div>
                            <label htmlFor="bio" className="block text-gray-700 font-medium mb-1">
                                Bio
                            </label>
                            <textarea
                                placeholder="Enter user bio"
                                name="bio"
                                className="w-full p-2 border rounded focus:outline-blue-500"
                                value={bio}
                                onChange={(e) => setBio(e.target.value)}
                            />
                        </div>
                    </div>
                    <button
                        type="submit"
                        disabled={loading}
                        className={`mt-4 w-full p-3 rounded text-white ${
                            loading ? "bg-gray-400 cursor-not-allowed" : "bg-blue-500 hover:bg-blue-600"
                        }`}
                    >
                        {loading ? "Saving..." : "Save Data"}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default Settings
