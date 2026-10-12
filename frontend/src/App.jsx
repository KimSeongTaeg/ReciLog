// [React 애플리케이션 라우팅 및 로그인 인증 보호 가드 설정 컴포넌트]
import React from 'react'; // 리액트 임포트
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'; // 리액트 라우터 임포트
import Login from './pages/Login'; // 로그인 페이지 임포트
import SignUp from './pages/SignUp'; // 회원가입 페이지 임포트
import Dashboard from './pages/Dashboard'; // 대시보드 페이지 임포트
import Expenses from './pages/Expenses'; // 지출 관리 페이지 임포트
import Navbar from './components/Navbar'; // 네비게이션바 컴포넌트 임포트

// [인증 보호 컴포넌트] - JWT 토큰이 없으면 로그인 페이지로 자동 리다이렉트 처리함
function ProtectedLayout({ children }) {
    const token = localStorage.getItem('accessToken'); // 로컬스토리지에서 JWT 토큰 확인
    if (!token) return <Navigate to="/login" replace />; // 토큰 미보유 시 로그인 이동

    return ( // 인증된 경우 화면 출력
        <div className="min-h-screen bg-slate-50">
            <Navbar /> {/* 공통 상단 네비게이션바 */}
            <main>{children}</main> {/* 자식 페이지 컴포넌트 출력 */}
        </div>
    );
}

export default function App() { // 앱 최상위 루트 컴포넌트
    return (
        <BrowserRouter> {/* 브라우저 히스토리 기반 라우팅 컨텍스트 제공 */}
            <Routes> {/* 라우트 매핑 매칭 시작 */}
                <Route path="/login" element={<Login />} /> {/* 로그인 경로 */}
                <Route path="/signup" element={<SignUp />} /> {/* 회원가입 경로 */}
                <Route
                    path="/dashboard"
                    element={
                        <ProtectedLayout>
                            <Dashboard /> {/* 인증 보호 대시보드 라우트 */}
                        </ProtectedLayout>
                    }
                />
                <Route
                    path="/expenses"
                    element={
                        <ProtectedLayout>
                            <Expenses /> {/* 인증 보호 지출 관리 라우트 */}
                        </ProtectedLayout>
                    }
                />
                <Route path="*" element={<Navigate to="/dashboard" replace />} /> {/* 기본 잘못된 경로 접속 시 대시보드로 이동 */}
            </Routes>
        </BrowserRouter>
    );
}