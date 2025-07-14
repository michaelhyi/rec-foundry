"use client";

import { useCallback, useState } from "react";
import { FcGoogle } from "react-icons/fc";
import { IoMdMail, IoMdLock } from "react-icons/io";
import { login } from "../http/auth";
import { useRouter } from "next/dist/client/components/navigation";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();

    const handleLogin = useCallback(async () => {
        const { userId } = await login(email, password);
        localStorage.setItem("userId", userId);
        router.push("/profile");
    }, [email, password, router]);

  return (
    <div className="flex items-center justify-center h-screen">
      <div className="w-full max-w-64">
        <h1 className="font-bold text-2xl text-center mb-6">Rec Foundry 🏀</h1>

        <label className="block text-gray-700 mb-1 text-sm text-light">Email</label>
        <div className="flex items-center border border-gray-300 rounded-lg px-3 py-2 mb-4">
          <IoMdMail className="text-gray-500 text-xl mr-2" />
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="outline-none w-full text-gray-700 placeholder-gray-400 text-sm"
          />
        </div>

        <label className="block text-gray-700 mb-1 text-sm text-light">Password</label>
        <div className="flex items-center border border-gray-300 rounded-lg px-3 py-2 mb-6">
          <IoMdLock className="text-gray-500 text-xl mr-2" />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="outline-none w-full text-gray-700 placeholder-gray-400 text-sm"
          />
        </div>

        <button onClick={handleLogin} className="mt-4 w-full bg-black text-white p-2 rounded-lg hover:opacity-80 cursor-pointer duration-200 transition">
          Login
        </button>
        <button className="flex items-center justify-center space-x-2 mt-3 w-full bg-white text-black border-black border p-2 rounded-lg hover:opacity-80 cursor-pointer duration-200 transition">
            <FcGoogle />
            <p>Login with Google</p>
        </button>
      </div>
    </div>
  );
}
