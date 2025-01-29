import Sidebar from "../../components/sidebar/Sidebar"
import "./Dashboard.css"
import PieChart from "../../components/pie_chart/PieChart"
import axios from "axios"
import {useEffect, useState} from "react"


function Dashboard() {

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
            .then(data => (countParam === "status") ? setStatusDate(data) : setPriorityData(data))
            .catch(error => console.error(error))
    }

    return (
        <div className={"dashboard"}>
            <Sidebar/>
            <div className="mainContent">
                <div>
                    <h1>
                        Welcome back {username}
                    </h1>
                </div>
                <div className={"charts"}>
                    <PieChart id={"status"} taskData={statusData} title={"Tasks by Status"}/>
                    <PieChart id={"priority"} taskData={priorityData} title={"Tasks by Priority"}/>
                </div>
            </div>
        </div>
    )
}

export default Dashboard