// [Axios HTTP 통신 클라이언트 및 JWT 인터셉터 모듈]
import axios from 'axios'; // 백엔드 통신용 axios 라이브러리 임포트

const API_BASE_URL = 'http://localhost:8080/api/v1'; // 백엔드 API 호스트 기준 기본 URL 정의

const api = axios.create({ // 기본 설정을 포함하는 axios 인스턴스 객체 생성
    baseURL: API_BASE_URL, // 기본 호스트 주소 지정
    headers: {
        'Content-Type': 'application/json', // 기본 데이터 요청 형식을 JSON으로 지정
    },
});

// [Axios 요청 인터셉터] - 백엔드로 나가는 모든 HTTP 요청 헤더에 로컬 스토리지의 Access Token을 자동으로 주입함
api.interceptors.request.use(
    (config) => { // 성공적인 요청 전처리 커스텀 콜백
        const token = localStorage.getItem('accessToken'); // 웹 브라우저 로컬스토리지에서 엑세스 토큰 획득
        if (token) { // 토큰이 존재할 경우
            config.headers.Authorization = `Bearer ${token}`; // Authorization 헤더에 Bearer 토큰 서식으로 등록
        }
        return config; // 수정된 요청 설정 반환
    },
    (error) => Promise.reject(error) // 요청 에러 발생 시 거절 프로미스 반환
);

// [Axios 응답 인터셉터] - 백엔드에서 401(Unauthorized) 응답이 올 경우 Refresh Token을 이용해 Access Token을 자동 재발급함
api.interceptors.response.use(
    (response) => response, // 정상 응답 시 그대로 패스스루 반환
    async (error) => { // 에러 처리 콜백
        const originalRequest = error.config; // 최초 실패했던 HTTP 요청 객체 정보 보존
        if (error.response?.status === 401 && !originalRequest._retry) { // 401 Unauthorized 에러이고 무한 재시도 플래그가 없는 경우
            originalRequest._retry = true; // 재시도 플래그를 true로 변경 (무한 루프 호출 차단)
            try {
                const refreshToken = localStorage.getItem('refreshToken'); // 저장된 리프레시 토큰 획득
                const res = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken }); // 토큰 재발급 요청 보내기

                localStorage.setItem('accessToken', res.data.accessToken); // 신규 발급받은 엑세스 토큰 스토리지에 업데이트
                originalRequest.headers.Authorization = `Bearer ${res.data.accessToken}`; // 기존 요청 헤더의 토큰 교체
                return api(originalRequest); // 기존 실패했던 HTTP 요청을 새로운 토큰으로 재시도 요청 실행
            } catch (refreshError) { // Refresh Token마저 만료되어 재발급 실패 시
                localStorage.clear(); // 브라우저 로컬 스토리지 세션 완전 초기화
                window.location.href = '/login'; // 강제로 로그인 페이지로 리다이렉트 이동
                return Promise.reject(refreshError); // 에러 반환
            }
        }
        return Promise.reject(error); // 그 외 일반 에러 반환
    }
);

export default api; // 설정이 완료된 axios 인스턴스 모듈 내보내기