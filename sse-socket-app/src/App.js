import './App.css';
import {useEffect, useState} from "react";

function App() {
    const [state, setState] = useState(null)
    const [currentTime, setCurrentTime] = useState(null)
    const [eventSource, setEventSource] = useState(null)

    useEffect(() => {
        const handleEvent = (e) => setCurrentTime(e.data)
        const es = new EventSource("http://localhost:8080/sse/stream-sse");

        es.onopen = (e) => setState(e.type)
        es.onerror = (event) => {
            if (event.eventPhase === EventSource.CLOSED) {
                setState("EventSource closed by server")
            } else {
                setState("EventSource error")
            }
            es.close();
        }
        es.addEventListener('time-event', handleEvent)

        setEventSource(es)

        return () => es.close();
    }, [])

    return (
        <div className="App">
            <header className="App-header">
                <div>
                    <div>Status: <span>{state}</span></div>
                    <div>{currentTime}</div>
                    <div>
                        <button onClick={() => eventSource.close()}>Stop</button>
                    </div>
                </div>
            </header>
        </div>
    );
}

export default App;
