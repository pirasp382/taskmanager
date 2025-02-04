import {Chart} from "chart.js/auto"
import {useEffect, useRef} from "react"
import "./Piechart.css"
import PropTypes from "prop-types"

function PieChart({taskData, id, title}) {
    const chartRef = useRef(null)

    useEffect(() => {
        if (chartRef.current) {
            chartRef.current.destroy()
        }

        if (taskData && taskData.length > 0) {
            const canvas = document.getElementById(`myChart${id}`)
            const ctx = canvas.getContext("2d")

            const data = {
                labels: taskData.map((task) => task.title),
                datasets: [
                    {
                        label: "Tasks",
                        data: taskData.map((task) => task.value),
                        backgroundColor: taskData.map((item) => item.color), // Farben aus den Daten
                        hoverOffset: 4,
                    },
                ],
            }

            chartRef.current = new Chart(ctx, {
                type: "pie",
                data: data,
            })
        }

        return () => {
            if (chartRef.current) {
                chartRef.current.destroy()
            }
        }
    }, [taskData])

    return (
        <div id={`pie_chart_${id}`} className="chart">
            <p>{title}</p>
            <canvas id={`myChart${id}`}></canvas>
        </div>
    )
}

PieChart.propTypes = {
    taskData: PropTypes.object,
    id: PropTypes.number,
    title: PropTypes.string,
}

export default PieChart

