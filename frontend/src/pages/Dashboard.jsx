// [대시보드 주요 통계 및 엑셀 다운로드 화면 컴포넌트]
import React, { useState } from 'react'; // 리액트 기본 훅 임포트
import api from '../services/api'; // 백엔드 연동용 커스텀 axios 모듈 임포트
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts'; // 리차트 도넛 차트 컴포넌트 임포트
import { DollarSign, FileSpreadsheet, ArrowUpRight } from 'lucide-react'; // 룩사이드 UI 아이콘 임포트

const COLORS = ['#6366F1', '#8B5CF6', '#EC4899', '#F59E0B', '#10B981']; // 차트 영역 구분을 위한 커스텀 색상 테마 팔레트

export default function Dashboard() { // 대시보드 컴포넌트 선언
    const [stats] = useState([ // 월별 카테고리 지출 통계 더미/초기 상태값
        { name: '식비', value: 320000 },
        { name: '카페/간식', value: 85000 },
        { name: '교통', value: 120000 },
        { name: '쇼핑', value: 210000 },
        { name: '기타', value: 45000 },
    ]);

    const totalExpense = stats.reduce((acc, curr) => acc + curr.value, 0); // 배열 수량을 모두 더해 월 총지출 누적 합계 계산

    // [엑셀 다운로드 핸들러 메서드]
    const handleExcelDownload = async () => {
        try {
            // responseType을 'blob'으로 지정하여 백엔드의 바이너리 스트림 데이터를 프론트엔드가 바이트 파일로 받아옴
            const response = await api.get('/expenses/excel', { responseType: 'blob' });
            const url = window.URL.createObjectURL(new Blob([response.data])); // 전달받은 바이너리 데이터를 임시 URL 객체로 변환
            const link = document.createElement('a'); // 가상의 <a> 태그 엘리먼트 동적 생성
            link.href = url; // 생성한 임시 URL을 링크에 세팅
            link.setAttribute('download', 'expenses.xlsx'); // 다운로드 실행 시 저장될 기본 파일 이름 설정
            document.body.appendChild(link); // DOM에 가상 링크 추가
            link.click(); // 프론트엔드 차원에서 클릭 이벤트를 강제로 발생시켜 다운로드 수행
            link.remove(); // 파일 다운로드 후 DOM 가상 엘리먼트 제거
        } catch (err) { // 예외 처리
            alert('엑셀 다운로드에 실패했습니다.'); // 사용자 에러 알림 팝업
        }
    };

    return ( // JSX 렌더링 시작
        <div className="max-w-7xl mx-auto px-4 py-8 space-y-8">
            {/* 1. 상단 현황 통계 카드 그리드 영역 */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex justify-between items-center">
                    <div>
                        <p className="text-sm font-medium text-slate-500">이번 달 총 지출</p>
                        <h3 className="text-2xl font-bold text-slate-900 mt-1">
                            {totalExpense.toLocaleString()} 원 {/* 원화 세자리마다 콤마 포맷팅 렌더링 */}
                        </h3>
                    </div>
                    <div className="p-3 bg-indigo-50 text-indigo-600 rounded-xl">
                        <DollarSign className="w-6 h-6" /> {/* 달러 아이콘 */}
                    </div>
                </div>

                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex justify-between items-center">
                    <div>
                        <p className="text-sm font-medium text-slate-500">이번 달 목표 예산</p>
                        <h3 className="text-2xl font-bold text-slate-900 mt-1">1,000,000 원</h3>
                        <p className="text-xs text-indigo-600 font-semibold mt-1">소진율 78%</p>
                    </div>
                    <div className="p-3 bg-emerald-50 text-emerald-600 rounded-xl">
                        <ArrowUpRight className="w-6 h-6" />
                    </div>
                </div>

                <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex justify-between items-center">
                    <div>
                        <p className="text-sm font-medium text-slate-500">지출 보고서</p>
                        <button
                            onClick={handleExcelDownload} // 클릭 이벤트에 엑셀 다운로드 로직 바인딩
                            className="mt-2 inline-flex items-center space-x-2 text-sm font-semibold text-white bg-emerald-600 hover:bg-emerald-700 px-4 py-2 rounded-xl transition shadow-sm"
                        >
                            <FileSpreadsheet className="w-4 h-4" /> {/* 엑셀 아이콘 */}
                            <span>엑셀 다운로드</span>
                        </button>
                    </div>
                </div>
            </div>

            {/* 2. 하단 시각화 차트 영역 */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
                <h3 className="text-lg font-bold text-slate-800 mb-4">카테고리별 지출 비율</h3>
                <div className="h-64">
                    <ResponsiveContainer width="100%" height="100%"> {/* 모바일/데스크톱 반응형 차트 컨테이너 */}
                        <PieChart>
                            <Pie
                                data={stats} // 차트에 매핑할 데이터 설정
                                cx="50%" // X축 중앙 정렬
                                cy="50%" // Y축 중앙 정렬
                                innerRadius={60} // 안쪽 원 가공 (도넛 모양 생성)
                                outerRadius={90} // 바깥 원 반지름
                                paddingAngle={5} // 조각 간 간격 여백
                                dataKey="value" // 수치 수량 기준 데이터 키
                            >
                                {stats.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} /> // 영역별 색상 설정
                                ))}
                            </Pie>
                            <Tooltip formatter={(val) => `${val.toLocaleString()}원`} /> {/* 마우스 호버 시 원화 세자리 콤마 수치 툴팁 출력 */}
                        </PieChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
}