import { createBrowserRouter, RouteObject } from "react-router-dom";
import Register from "./pages/register/Register";
import Home from "./pages/home/Home"
import Login from "./pages/login/Login"
import Profile from "./pages/profile/Profile";
import Dashboard from "./pages/dashboard/Dashboard";

const router: RouteObject[] = [
    {
        path: "/",
        element: <Home />
    },
    {
        path: "/register",
        element: <Register />
    },
    {
        path: "/login",
        element: <Login />
    },
    {
        path: "/profile",
        element: <Profile />
    },
    {
        path: "/dashboard",
        element: <Dashboard />
    }

];

const routerConfig = createBrowserRouter(router);

export default routerConfig;
