import React, { useState } from 'react';
import api from '../services/api';
import { Upload, Camera, X, Loader2 } from 'lucide-react';

export default function OcrUploadModal({ isOpen, onClose, onSuccess }) {
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);

    if (!isOpen) return null;

    const handleFileChange = (e) => {
        if (e.target.files[0]) {
            setFile(e.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!file) return;
        setLoading(true);

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await api.post('/receipts/ocr', formData, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });
            onSuccess(res.data);
            onClose();
        } catch (err) {
            alert('영수증 인식에 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
            <div className="bg-white w-full max-w-md rounded-2xl p-6 shadow-xl relative">
                <button
                    onClick={onClose}
                    className="absolute top-4 right-4 text-slate-400 hover:text-slate-600"
                >
                    <X className="w-5 h-5" />
                </button>

                <h3 className="text-xl font-bold text-slate-900 flex items-center gap-2">
                    <Camera className="w-5 h-5 text-indigo-600" />
                    영수증 AI 자동 인식
                </h3>
                <p className="text-sm text-slate-500 mt-1">
                    영수증 이미지 파일(.jpg, .png)을 선택해 주세요.
                </p>

                <div className="mt-6 border-2 border-dashed border-slate-200 rounded-xl p-8 text-center hover:border-indigo-400 transition cursor-pointer relative">
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleFileChange}
                        className="absolute inset-0 opacity-0 cursor-pointer"
                    />
                    <Upload className="w-10 h-10 text-slate-400 mx-auto mb-2" />
                    <span className="text-sm font-medium text-slate-600">
            {file ? file.name : '클릭하여 영수증 이미지 선택'}
          </span>
                </div>

                <button
                    onClick={handleUpload}
                    disabled={!file || loading}
                    className="w-full mt-6 bg-indigo-600 hover:bg-indigo-700 disabled:bg-slate-300 text-white font-semibold py-3 rounded-xl transition flex items-center justify-center gap-2"
                >
                    {loading ? (
                        <>
                            <Loader2 className="w-5 h-5 animate-spin" />
                            <span>영수증 분석 중...</span>
                        </>
                    ) : (
                        <span>OCR 분석 시작</span>
                    )}
                </button>
            </div>
        </div>
    );
}