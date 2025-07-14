import Navbar from "./Navbar";
import Sidebar from "./Sidebar";

export default function Container({ children, active }: { children: React.ReactNode, active?: string }) {
    return (
        <main className="flex">
            <Sidebar active={active} /> 
            <div className="flex flex-col w-full">
                <Navbar />
                {children}
            </div>
        </main>
    );
}