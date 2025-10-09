import {ProtectedRoute} from "../../components/ProtectedRoute";
export default function Chat(){
    return(
        <ProtectedRoute>
            <h1>Chat</h1>
        </ProtectedRoute>
    )
}