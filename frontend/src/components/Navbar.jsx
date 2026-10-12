import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { LayoutDashboard, Receipt, LogOut } from 'lucide-react';

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const username = localStorage.getItem('username') || '사용자';

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const isActive = (path) => location.pathname === path;

    return (
        <nav className="bg-white border-b border-slate-200 sticky top-0 z-30">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center space-x-8">
                        <Link to="/dashboard" className="flex items-center space-x-2">
                            <div className="p-2 bg-indigo-600 rounded-lg text-white">
                                <Receipt className="w-5 h-5" />
                            </div>
                            <span className="text-xl font-bold bg-gradient-to-r from-indigo-600 to-violet-600 bg-clip-text text-transparent">
                ReciLog
              </span>
                        </Link>

                        <div className="hidden sm:flex space-x-4">
                            <Link
                                to="/dashboard"
                                className={`flex items-center space-x-2 px-3 py-2 rounded-md text-sm font-medium transition ${
                                    isActive('/dashboard') ? 'bg-indigo-50 text-indigo-600' : 'text-slate-600 hover:text-slate-900'
                                }`}
                            >
                                <LayoutDashboard className="w-4 h-4" />
                                <span>대시보드</span>
                            </Link>
                            <Link
                                to="/expenses"
                                className={`flex items-center space-x-2 px-3 py-2 rounded-md text-sm font-medium transition ${
                                    isActive('/expenses') ? 'bg-indigo-50 text-indigo-600' : 'text-slate-600 hover:text-slate-900'
                                }`}
                            >
                                <Receipt className="w-4 h-4" />
                                <span>지출 내역</span>
                            </Link>
                        </div>
                    </div>

                    <div className="flex items-center space-x-4">
            <span className="text-sm font-medium text-slate-700">
              <span className="text-indigo-600 font-bold">{username}</span> 님
            </span>
                        <button
                            onClick={handleLogout}
                            className="p-2 text-slate-500 hover:text-red-600 hover:bg-slate-100 rounded-lg transition"
                            title="로그아웃"
                        >
                            <LogOut className="w-5 h-5" />
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
}