"use client";

import { useState, useEffect } from "react";
import Container from "../components/Container";
import { getPlayer, createPlayer } from "../http/player";

export default function Profile() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [formData, setFormData] = useState({
        bio: "",
        dateOfBirth: "",
        height: "",
        position: "",
        weight: "",
        yearsOfExperience: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await createPlayer(localStorage.getItem("userId") || "", formData);
    };

    useEffect(() => {
        (async () => {
            const player = await getPlayer(localStorage.getItem("userId") || "");

            setFirstName(player.firstName);
            setLastName(player.lastName);
            setFormData((prev) => ({
                ...prev,
                bio: player.bio,
                dateOfBirth: player.dateOfBirth,
                height: player.height,
                position: player.position,
                weight: player.weight,
                yearsOfExperience: player.yearsOfExperience,
            }));
        })();
    }, []);

    return (
        <Container active="profile">
            <form onSubmit={handleSubmit} className="mt-18 max-w-xl mx-auto p-6 bg-white rounded-lg shadow-md space-y-4 border border-gray-200">
                <h2 className="text-3xl font-semibold">{firstName} {lastName}</h2>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Bio</label>
                    <input
                        name="bio"
                        value={formData.bio}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md"
                        placeholder="Tell us about yourself"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Date of Birth</label>
                    <input
                        type="date"
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md"
                    />
                </div>

                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Height</label>
                        <input
                            name="height"
                            value={formData.height}
                            onChange={handleChange}
                            className="w-full p-2 border border-gray-300 rounded-md"
                            placeholder="e.g. 6'2"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Weight</label>
                        <input
                            name="weight"
                            value={formData.weight}
                            onChange={handleChange}
                            className="w-full p-2 border border-gray-300 rounded-md"
                            placeholder="e.g. 180 lbs"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Position</label>
                    <input
                        name="position"
                        value={formData.position}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md"
                        placeholder="e.g. Guard"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Years of Experience</label>
                    <input
                        name="yearsOfExperience"
                        value={formData.yearsOfExperience}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md"
                        placeholder="e.g. 3"
                    />
                </div>

                <button
                    type="submit"
                    className="w-full mt-4 bg-black text-white p-2 rounded-lg hover:opacity-80 cursor-pointer duration-200 transition"
                >
                    Save Changes
                </button>
            </form>
        </Container>
    );
}