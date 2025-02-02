import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import App from './App'
import {LanguageProvider} from "./components/language_context/LanguageContext"
import {DarkModeProvider} from "./components/dark_mode_context/DarkModeContext"

const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(
    <LanguageProvider>
        <DarkModeProvider>
            <App/>
        </DarkModeProvider>
    </LanguageProvider>,
)

