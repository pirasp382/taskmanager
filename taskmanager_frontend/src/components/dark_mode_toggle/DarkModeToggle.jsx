import {useDarkMode} from "../dark_mode_context/DarkModeContext"
import {CiSun} from "react-icons/ci"
import {FiMoon} from "react-icons/fi"
import "./DarkModeToggle.css"

export default function DarkModeToggle() {
    const {darkMode, toggleDarkMode} = useDarkMode()

    return (
        <button
            onClick={() => toggleDarkMode(!darkMode)}
            className="dark-mode-toggle-btn"
        >
            {darkMode ? <div className={"icon_field"}><FiMoon className={"icon"}/> <p>Dark Mode</p></div> :
                <div className={"icon_field"}><CiSun className={"icon"}/><p>Light Mode</p></div>}
        </button>
    )
}
