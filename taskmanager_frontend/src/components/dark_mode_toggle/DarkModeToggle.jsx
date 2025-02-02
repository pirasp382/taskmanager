import {useDarkMode} from "../dark_mode_context/DarkModeContext" // Kontext importieren
import "./DarkModeToggle.css"

export default function DarkModeToggle() {
    const {darkMode, toggleDarkMode} = useDarkMode()

    return (
        <button
            onClick={() => toggleDarkMode(!darkMode)} // Toggle über den Kontext
            className="dark-mode-toggle-btn"
        >
            {darkMode ? "🌙 Dark Mode" : "☀️ Light Mode"}
        </button>
    )
}
