import Menubar from "../components/menubar.jsx";
import Header from "../components/header.jsx";

const Home = () => {
    return (
        <div className="flex flex-col items-center justify-content-center min-vh-100">
            <Menubar />
            <Header />
        </div>
    )
}

export default Home;