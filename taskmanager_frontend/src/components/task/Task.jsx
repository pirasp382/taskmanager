import "./Task.css"
import PropTypes from 'prop-types'
import {MdDeleteForever} from "react-icons/md"

function Task({id, title, description, status, priority, createdDate, lastUpdate, getTaskById, deleteTaskById}) {


    function deleteItem() {
        deleteTaskById(id)
    }

    function openTask() {
        getTaskById(id)
    }

    return <div className="Task_Outer">
        <MdDeleteForever className={"task-checkbox"} onClick={deleteItem}/>
        <div className={"Task"} onClick={openTask}>
            <p>{title}</p>
            <p className={"status"} style={{backgroundColor: status.color}}>{status.title}</p>
            <p className={"priority"} style={{backgroundColor: priority.color}}>{priority.title}</p>
        </div>
    </div>
}

Task.propTypes = {
    id: PropTypes.number,
    title: PropTypes.string,
    description: PropTypes.string,
    status: PropTypes.object,
    priority: PropTypes.object,
    createdDate: PropTypes.string,
    lastUpdate: PropTypes.string,
    getTaskById: PropTypes.func,
    deleteTaskById: PropTypes.func,
}

export default Task