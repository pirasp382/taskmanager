import Task from "../task/Task"
import "./Tasklist.css"
import PropTypes from 'prop-types'

import {BiDownArrow, BiUpArrow} from "react-icons/bi"
import {useState} from "react"
import {useLanguage} from "../language_context/LanguageContext"
import texts from "../../data/texts"

function Tasklist({tasks, deleteTaskById, getTaskById, sortTasks}) {

    const [titleSort, setTitleSort] = useState(true)
    const [statusSort, setStatusSort] = useState(true)
    const [prioritySort, setPrioritySort] = useState(true)
    const {language} = useLanguage();

    function titleSorted() {
        setTitleSort(!titleSort)
        sortTasks("title", titleSort)
    }

    function statusSorted() {
        setStatusSort(!statusSort)
        sortTasks("status", statusSort)
    }

    function prioritySorted() {
        setPrioritySort(!prioritySort)
        sortTasks("priority", prioritySort)
    }

    return (
        <div className={"tasklist"}>
            <div className={"header"}>
                <h2>{texts.tasklist.overview[language]}</h2>
                <div className={"headerCol"}>
                    <p>{texts.tasklist.title[language]} {titleSort && <BiDownArrow className={"icon"} onClick={titleSorted}/>}
                        {!titleSort && <BiUpArrow className={"icon"} onClick={titleSorted}/>}</p>
                    <p>{texts.tasklist.status[language]} {statusSort &&
                        <BiDownArrow className={"icon"} onClick={statusSorted}/>}
                        {!statusSort && <BiUpArrow className={"icon"} onClick={statusSorted}/>}</p>
                    <p>{texts.tasklist.priority[language]} {prioritySort &&
                        <BiDownArrow className={"icon"} onClick={prioritySorted}/>}
                        {!prioritySort &&
                            <BiUpArrow className={"icon"} onClick={prioritySorted}/>}</p>
                </div>
            </div>
            <div className={"tasks"}>
                {tasks.map(task => <Task id={task.id} title={task.title} status={task.status}
                                         description={task.description}
                                         priority={task.priority} lastUpdate={task.lastUpdate}
                                         createdDate={task.createdDate} deleteTaskById={deleteTaskById}
                                         getTaskById={getTaskById}/>)}
            </div>
        </div>
    )
}

Tasklist.propTypes = {
    tasks: PropTypes.array,
    deleteTaskById: PropTypes.func,
    getTaskById: PropTypes.func,
    sortTasks: PropTypes.func,
}

export default Tasklist