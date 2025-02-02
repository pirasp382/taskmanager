import Sidebar from "../../components/sidebar/Sidebar"
import "./Dashboard.css"
import PieChart from "../../components/pie_chart/PieChart"
import axios from "axios"
import {useEffect, useState} from "react"
import texts from "../../data/texts"
import {useLanguage} from "../../components/language_context/LanguageContext"
import DarkModeToggle from "../../components/dark_mode_toggle/DarkModeToggle"


function Dashboard() {
    const {language, changeLanguage} = useLanguage()
    const [statusData, setStatusDate] = useState()
    const [priorityData, setPriorityData] = useState()
    const username = localStorage.getItem("username")

    useEffect(() => {
        getCountsTasksByParam("status")
        getCountsTasksByParam("priority")
    }, [])

    function getCountsTasksByParam(countParam) {
        const get_counts_url = `http://localhost:8000/dashboard/pie?countByValue=${countParam}`
        const token = localStorage.getItem("jwt")
        axios.get(get_counts_url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => {
                (countParam === "status") ? setStatusDate(data.tasksDetails) : setPriorityData(data.tasksDetails)
                localStorage.setItem("jwt", data.token)
            })
            .catch(error => console.error(error))
    }

    return (
        <div className={"dashboard"}>
            <Sidebar/>
            <div className={"dark_mode_toggle"}>
                <DarkModeToggle/>
            </div>
            <div className="mainContent">
                <div>
                    <h1>
                        {texts.dashboard.welcome[language]} {username}
                    </h1>
                </div>
                <div className={"charts"}>
                    <PieChart id={"status"} taskData={statusData} title={texts.dashboard.tasksByStatus[language]}/>
                    <PieChart id={"priority"} taskData={priorityData}
                              title={texts.dashboard.tasksByPriority[language]}/>
                </div>
            </div>
        </div>
    )
}

export default Dashboard