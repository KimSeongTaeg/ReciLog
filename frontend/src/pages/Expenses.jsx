import React, { useState, useEffect } from 'react';
import api from '../services/api';
import OcrUploadModal from './OcrUploadModal';
import { Camera, Search, Plus } from 'lucide-react';

export default function Expenses() {
    const [expenses, setExpenses] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchExpenses = async () => {
        try {
            const res = await api.get('/expenses', { params: { keyword } });
            setExpenses(res.data.content || []);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchExpenses();
    }, [keyword]);

    const handleOcrSuccess = async (ocrData) => {
        try {
            await api.post('/expenses', {
                categoryId: 1,
                storeName: ocrData.storeName || 'OCR 가맹점',
                totalAmount: ocrData.totalAmount || 15000,
                spentAt: new Date().toISOString(),
                paymentMethod: 'CARD',
                memo: '영수증 OCR 자동 저장',
                items: []
            });
            alert('영수증 지출 내역이 자동으로 등록되었습니다!');
            fetchExpenses();
        } catch (err) {
            alert('지출 등록에 실패했습니다.');
        }
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-8 space-y-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                <h2 className="text-2xl font-bold text-slate-900">지출 관리</h2>

                <button
                    onClick={() => setIsModalOpen(true)}
                    className="inline-flex items-center space-x-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2.5 rounded-xl font-semibold transition shadow-sm"
                >
                    <Camera className="w-5 h-5" />
                    <span>영수증 스캔 등록</span>
                </button>
            </div>

            <div className="relative max-w-md">
                <Search className="w-5 h-5 text-slate-400 absolute left-3 top-3" />
                <input
                    type="text"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    placeholder="상호명 또는 메모 검색..."
                    className="w-full pl-10 pr-4 py-2 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
            </div>

            <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
                <table className="w-full text-left border-collapse">
                    <thead>
                    <tr className="bg-slate-50 border-b border-slate-100 text-slate-500 text-sm">
                        <th className="p-4">날짜</th>
                        <th className="p-4">카테고리</th>
                        <th className="p-4">상호명</th>
                        <th className="p-4">결제수단</th>
                        <th className="p-4 text-right">금액</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100 text-sm">
                    {expenses.length > 0 ? (
                        expenses.map((item) => (
                            <tr key={item.expenseId} className="hover:bg-slate-50/50">
                                <td className="p-4 text-slate-500">{item.spentAt?.substring(0, 10)}</td>
                                <td className="p-4 font-medium text-indigo-600">{item.categoryName}</td>
                                <td className="p-4 font-semibold text-slate-800">{item.storeName}</td>
                                <td className="p-4 text-slate-500">{item.paymentMethod}</td>
                                <td className="p-4 text-right font-bold text-slate-900">
                                    {item.totalAmount?.toLocaleString()} 원
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="p-8 text-center text-slate-400">
                                등록된 지출 내역이 없습니다.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            <OcrUploadModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onSuccess={handleOcrSuccess}
            />
        </div>
    );
}