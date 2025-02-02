import "./Tasks.css"
import Tasklist from "../../components/tasklist/Tasklist"
import {useEffect, useState} from "react"
import axios from "axios"
import Sidebar from "../../components/sidebar/Sidebar"
import Modal from "../../components/modal/Modal"
import TaskDetailModal from "../../components/task_detail_modal/TaskDetailModal"
import {IoMdAddCircleOutline} from "react-icons/io"
import {useLanguage} from "../../components/language_context/LanguageContext"
import texts from "../../data/texts"

function Tasks() {

    const [tasks, setTasks] = useState([])
    const [singleTask, setSingleTask] = useState("")
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [showSingleTaskModal, setShowSingleTaskModal] = useState(false)
    const {language} = useLanguage()

    useEffect(() => {
        getAllTasks()
    }, [])


    function getAllTasks() {
        const get_all_tasks_url = "http://localhost:8000/task"
        const token = localStorage.getItem("jwt")
        axios.get(get_all_tasks_url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => {
                setTasks(data["taskList"])
                localStorage.setItem("jwt", data.token)
            })
            .catch(error => console.log(error))

    }

    function handleAddTask(task) {
        const add_task_url = "http://localhost:8000/task"
        const token = localStorage.getItem("jwt")
        const status = new Object()
        status.title = task.status
        const priority = new Object()
        priority.title = task.priority
        axios.post(add_task_url, {
            title: task.title,
            description: task.description,
            status: status,
            priority: priority,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => localStorage.setItem("jwt", data.token))
            .catch(error => console.log(error))
            .finally(() => getAllTasks())
    }

    function handleUpdateTask(task, id) {
        const update_task_url = `http://localhost:8000/task/${id}`
        const token = localStorage.getItem("jwt")
        const status = new Object()
        status.title = task.status
        const priority = new Object()
        priority.title = task.priority
        axios.put(update_task_url, {
            title: task.title,
            description: task.description,
            status: status,
            priority: priority,
        }, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => localStorage.setItem("jwt", data.token))
            .catch(error => console.error(error))
            .finally(() => getAllTasks())
    }

    function handleGetTaskById(id) {
        const get_task_url = "http://localhost:8000/task/tasks/" + id
        const token = localStorage.getItem("jwt")
        axios.get(get_task_url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => {
                setSingleTask(data.taskList[0])
                localStorage.setItem("jwt", data.token)
            })
            .then(() => setShowSingleTaskModal(true))
            .catch(error => console.error(error))
    }

    function handleDeleteTask(taskId) {
        const delete_task_url = "http://localhost:8000/task/" + taskId
        const token = localStorage.getItem("jwt")
        axios.delete(delete_task_url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .catch(error => console.error(error))
            .finally(() => getAllTasks())
    }

    function getAllTasksSorted(sortValue, direction) {
        const sort_task_url = `http://localhost:8000/task/sort?sorted=${sortValue}&direction=${direction}`
        const token = localStorage.getItem("jwt")
        axios.get(sort_task_url, {
            headers: {
                'Authorization': `Bearer: ${token}`,
            },
        })
            .then(response => response.data)
            .then(data => {
                setTasks(data.taskList)
                localStorage.setItem("jwt", data.token)
            })
            .catch(error => console.error(error))
    }

    return (
        <div className={"homePage"}>
            <Sidebar/>
            <div className="mainContent">
                <button onClick={() => setIsModalOpen(true)} className="openModalButton">
                    <IoMdAddCircleOutline className={"add-icon"}/> {texts.tasks.add_task[language]}
                </button>
                <Tasklist tasks={tasks} deleteTaskById={handleDeleteTask} getTaskById={handleGetTaskById}
                          sortTasks={getAllTasksSorted}/>
            </div>
            <div className={"addTaskModal"}>
                {isModalOpen && (
                    <Modal
                        onClose={() => setIsModalOpen(false)}
                        onSubmit={handleAddTask}
                    />
                )}
            </div>
            {showSingleTaskModal &&
                <TaskDetailModal task={singleTask} onClose={() => setShowSingleTaskModal(false)}
                                 onDelete={handleDeleteTask} onUpdate={handleUpdateTask}/>
            }
        </div>
    )
}

export default Tasks