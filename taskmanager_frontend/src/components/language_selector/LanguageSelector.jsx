import React, {useState} from "react"
import "./LanguageSelector.css"
import {CiGlobe} from "react-icons/ci"
import languages from "../../data/languages"
import {useLanguage} from "../language_context/LanguageContext"
import texts from "../../data/texts"

const LanguageSelector = () => {
    const [isOpen, setIsOpen] = useState(false)
    const {language, changeLanguage} = useLanguage()

    const toggleDropdown = () => {
        setIsOpen(!isOpen)
    }

    function languageChange(language) {
        changeLanguage(language)
        toggleDropdown()
    }

    return (
        <div className="language-selector-container">

            <button className="language-button" onClick={toggleDropdown}>
                <CiGlobe/> {texts.language_selector[language]}
            </button>

            <ul className={`dropdown-menu ${isOpen ? "open" : ""}`}>
                {Object.entries(languages).map(([key, value]) => (
                    <li
                        key={key}
                        className="dropdown-item"
                        onClick={(e) => languageChange(key)}
                    >
                        {key} <i>{value}</i>
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default LanguageSelector
