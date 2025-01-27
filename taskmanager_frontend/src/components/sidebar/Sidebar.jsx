import React from "react"
import "./Sidebar.css"
import {IoIosLogOut} from "react-icons/io"
import {CiBoxList, CiSettings} from "react-icons/ci"
import {MdDashboard} from "react-icons/md"
import {useNavigate} from "react-router-dom"
import LanguageSelector from "../language_selector/LanguageSelector"
import {useLanguage} from "../language_context/LanguageContext"
import texts from "../../data/texts"

function Sidebar() {
    const navigate = useNavigate()
    const {language} = useLanguage();
    function onLogout() {
        localStorage.clear()
        navigate("/login")
    }

    return (
        <div className="sidebar">
            <ul className="sidebar-menu">
                <li onClick={() => navigate("/")}><MdDashboard/> {texts.sidebar.dashboard[language]}</li>
                <li onClick={() => navigate("/tasks")}>
                    <CiBoxList/> {texts.sidebar.tasks[language]}
                </li>
                <li><CiSettings/> {texts.sidebar.settings[language]}</li>
            </ul>
            <div className={"language-selector"}>
                <LanguageSelector/>
            </div>
            <button className="logout-button" onClick={onLogout}>
                <IoIosLogOut className={"logout-icon"}/>
                {texts.sidebar.logout[language]}
            </button>
        </div>
    )
}


export default Sidebar
