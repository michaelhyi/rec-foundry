import { CiBellOn } from "react-icons/ci";
import { IoMdPerson, IoMdSearch } from "react-icons/io";

export default function Navbar() {
    return (
        <nav className="flex items-center p-6 border-b-1 border-gray-200 h-20 w-full">
            <div className="flex items-center border border-gray-300 rounded-lg px-3 py-2 w-full max-w-md">
                <IoMdSearch className="text-gray-500 text-xl mr-2" />
                <input
                    type="text"
                    placeholder="Search..."
                    className="outline-none w-full text-gray-700 placeholder-gray-400"
                />
            </div>
            <div className="flex items-center space-x-10 ml-auto pr-4">
                <CiBellOn size={24}/>
                <div className="bg-gray-300 text-white rounded-full p-2 w-10">
                    <IoMdPerson size={24}/>
                </div>
            </div>
        </nav>
    );
}