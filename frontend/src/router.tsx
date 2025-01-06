import { createBrowserRouter, RouteObject } from "react-router-dom";
import Register from "./pages/register/Register";
import Login from "./pages/login/Login"

const router: RouteObject[] = [
    {
        path: "/",
        element: <Register />
    },
    {
        path: "/login",
        element: <Login />
    }
];

const routerConfig = createBrowserRouter(router);

export default routerConfig;
