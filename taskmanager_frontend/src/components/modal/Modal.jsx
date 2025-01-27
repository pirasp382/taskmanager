import "./Modal.css"
import {useEffect, useState} from "react"
import PropTypes from 'prop-types'
import {useLanguage} from "../language_context/LanguageContext"
import texts from "../../data/texts"

function Modal({onClose, onSubmit}) {
    const [taskTitle, setTaskTitle] = useState("")
    const [taskDescription, setTaskDescription] = useState("")
    const [selectedStatus, setSelectedStatus] = useState("")
    const [selectedPriority, setSelectedPriority] = useState("")
    const statuses = JSON.parse(localStorage.getItem("status")) || []
    const priorities = JSON.parse(localStorage.getItem("priority")) || []
    const {language} = useLanguage()


    useEffect(() => {
        const handleKeyDown = (event) => {
            if (event.keyCode === 27) {
                onClose()
            }
        }
        window.addEventListener("keydown", handleKeyDown)
        return () => window.removeEventListener("keydown", handleKeyDown)
    }, [onClose])

    function handleSubmit(e) {
        e.preventDefault()
        const task = {
            title: taskTitle,
            description: taskDescription,
            status: selectedStatus,
            priority: selectedPriority,
        }
        onSubmit(task)
        onClose()
    }

    return (
        <div className="modalOverlay">
            <div className="modalContent">
                <h2>{texts.add_task.add_new_task[language]}</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder={texts.add_task.task_title[language]}
                        value={taskTitle}
                        onChange={(e) => setTaskTitle(e.target.value)}
                        required
                    />
                    <textarea
                        placeholder={texts.add_task.task_description[language]}
                        value={taskDescription}
                        onChange={(e) => setTaskDescription(e.target.value)}
                        rows="5"
                    />
                    <div className="selectRow">
                        <div className="selectWrapper">
                            <label>{texts.add_task.status[language]}</label>
                            <select
                                className="statusSelect"
                                value={selectedStatus}
                                onChange={(e) => setSelectedStatus(e.target.value)}
                            >
                                <option value="">{texts.add_task.select_status[language]}</option>
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
                            <label>{texts.add_task.priority[language]}</label>
                            <select
                                className="prioritySelect"
                                value={selectedPriority}
                                onChange={(e) => setSelectedPriority(e.target.value)}
                            >
                                <option value="">{texts.add_task.selec_priority[language]}</option>
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
                        <button type="submit" className="submitButton">
                            {texts.add_task.add[language]}
                        </button>
                        <button type="button" className="cancelButton" onClick={onClose}>
                            {texts.add_task.cancel[language]}
                        </button>
                    </div>
                </form>
            </div>

        </div>
    )
}

Modal.propTypes = {
    onClose: PropTypes.func,
    onSubmit: PropTypes.func,
}

export default Modal
