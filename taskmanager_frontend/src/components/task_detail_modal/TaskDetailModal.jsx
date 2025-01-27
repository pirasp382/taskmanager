import {useEffect, useState} from "react"
import PropTypes from 'prop-types'

import "./TaskDetailModal.css"
import {useLanguage} from "../language_context/LanguageContext"
import texts from "../../data/texts"

function TaskDetailModal({task, onClose, onUpdate, onDelete}) {
    const [title, setTitle] = useState(task.title)
    const [description, setDescription] = useState(task.description || "")
    const [selectedStatus, setSelectedStatus] = useState("")
    const [selectedPriority, setSelectedPriority] = useState("")
    const statuses = JSON.parse(localStorage.getItem("status")) || []
    const priorities = JSON.parse(localStorage.getItem("priority")) || []
    const {language} = useLanguage();

    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.keyCode === 27) {
                onClose()
            }
        }
        window.addEventListener("keydown", handleKeyDown)
        return () => window.removeEventListener("keydown", handleKeyDown)
    }, [onClose])

    function handleUpdate() {
        const updatedTask = {}
        if (title !== task.title) updatedTask.title = title
        if (description !== task.description) updatedTask.description = description
        if (selectedStatus && selectedStatus !== task.status)
            updatedTask.status = selectedStatus
        if (selectedPriority && selectedPriority !== task.priority)
            updatedTask.priority = selectedPriority

        if (Object.keys(updatedTask).length > 0) {
            onUpdate(updatedTask, task.id)
        }
        onClose()
    }

    function handleDelete() {
        onDelete(task.id)
        onClose()
    }

    return (
        <div className="modalOverlay">
            <div className="modalContent">
                <h2>{texts.detailed_task_modal.task_details[language]}</h2>
                <div className="formGroup">
                    <label htmlFor="title">{texts.detailed_task_modal.task_title[language]}</label>
                    <input
                        id="title"
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                </div>
                <div className="formGroup">
                    <label htmlFor="description">{texts.detailed_task_modal.task_description[language]}</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                </div>
                <div className="selectContainer">
                    <div className="selectWrapper">
                        <label>{texts.detailed_task_modal.status[language]}</label>
                        <select
                            className="statusSelect"
                            value={selectedStatus || task.status.title}
                            onChange={(e) => setSelectedStatus(e.target.value)}
                        >
                            <option value="">Select Status</option>
                            {statuses.map((item) => (
                                <option
                                    key={item.title}
                                    value={item.title}
                                    style={{backgroundColor: item.color}}
                                >
                                    {item.title}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="selectWrapper">
                        <label>{texts.detailed_task_modal.priority[language]}</label>
                        <select
                            className="prioritySelect"
                            value={selectedPriority || task.priority.title}
                            onChange={(e) => setSelectedPriority(e.target.value)}
                        >
                            <option value="">Select Priority</option>
                            {priorities.map((item) => (
                                <option
                                    key={item.title}
                                    value={item.title}
                                    style={{backgroundColor: item.color}}
                                >
                                    {item.title}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="modalActions">
                    <button onClick={handleUpdate} className="updateButton">
                        {texts.detailed_task_modal.update[language]}
                    </button>
                    <button onClick={handleDelete} className="deleteButton">
                        {texts.detailed_task_modal.delete[language]}
                    </button>
                    <button onClick={onClose} className="cancelButton">
                        {texts.detailed_task_modal.cancel[language]}
                    </button>
                </div>
            </div>
        </div>
    )
}

TaskDetailModal.propTypes={
    task: PropTypes.object,
    onClose: PropTypes.func,
    onUpdate: PropTypes.func,
    onDelete:PropTypes.func
}


export default TaskDetailModal
