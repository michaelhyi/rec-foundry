"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback } from "react";
import { CgGym } from "react-icons/cg";
import { IoMdExit, IoMdPerson } from "react-icons/io";

export default function Sidebar({active = "profile"}) {
    const router = useRouter();

    const handleLogout = useCallback(() => {
        localStorage.removeItem("userId");
        router.push("/login");
    }, [router]);

    return (
        <div className="p-4 h-screen border-r-1 border-gray-200 w-1/5">
            <h1 className="font-bold text-2xl">Rec Foundry</h1>
            <div className="mt-6 flex flex-col space-y-2">
                <Link className={`flex items-center space-x-2 text-gray-500 px-2 py-1 rounded-lg outline-none ${active === 'profile' ? 'bg-gray-100' : ''} hover:opacity-50 duration-200`} href="/profile">
                    <IoMdPerson/>
                    <p className="font-medium">Profile</p>
                </Link>
                <Link className={`flex items-center space-x-2 text-gray-500 px-2 py-1 rounded-lg outline-none ${active === 'gyms' ? 'bg-gray-100' : ''} hover:opacity-50 duration-200`} href="/gyms">
                    <CgGym />
                    <p className="font-medium">Gyms</p>
                </Link>
                <button onClick={handleLogout} className="mt-8 flex items-center space-x-2 text-red-400 px-2 py-1 rounded-lg cursor-pointer hover:opacity-50 duration-200">
                    <IoMdExit/>
                    <p className="font-medium">Logout</p>
                </button>
            </div>
        </div>
    );
}