document.addEventListener("DOMContentLoaded", function() {
    // 여기에 DOM이 로드된 후 실행할 코드를 작성합니다.
    console.log("DOM이 완성되었습니다!");

    const emailInput = document.getElementById('email');
    const codeInput = document.getElementById('code');
    const verificationMessage = document.getElementById('verificationMessage');
    let verifiedEmail = null;

    function showVerificationMessage(message, success = false) {
        verificationMessage.textContent = message;
        verificationMessage.className = success ? 'message success' : 'message';
    }

    async function requestVerification(path, body) {
        const response = await fetch(path, {
            method: 'POST',
            credentials: 'same-origin',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const result = await response.json().catch(() => ({ success: false, message: '서버 응답을 읽을 수 없습니다.' }));
        if (!response.ok || !result.success) {
            throw new Error(result.message || '이메일 인증 요청에 실패했습니다.');
        }
        return result;
    }

    document.getElementById('sendCodeButton').addEventListener('click', async function () {
        const email = emailInput.value.trim();
        if (!emailInput.checkValidity()) {
            emailInput.reportValidity();
            return;
        }
        this.disabled = true;
        try {
            const result = await requestVerification('/api/email-verifications', { email });
            verifiedEmail = null;
            codeInput.value = '';
            showVerificationMessage(result.message, true);
        } catch (error) {
            showVerificationMessage(error.message);
        } finally {
            this.disabled = false;
        }
    });

    document.getElementById('verifyCodeButton').addEventListener('click', async function () {
        const email = emailInput.value.trim();
        const code = codeInput.value.trim();
        if (!emailInput.checkValidity()) {
            emailInput.reportValidity();
            return;
        }
        if (!/^\d{6}$/.test(code)) {
            showVerificationMessage('인증번호는 6자리 숫자입니다.');
            return;
        }
        this.disabled = true;
        try {
            const result = await requestVerification('/api/email-verifications/verify', { email, code });
            verifiedEmail = email.toLowerCase();
            showVerificationMessage(result.message, true);
        } catch (error) {
            showVerificationMessage(error.message);
        } finally {
            this.disabled = false;
        }
    });

    emailInput.addEventListener('input', function () {
        if (verifiedEmail !== null && verifiedEmail !== this.value.trim().toLowerCase()) {
            verifiedEmail = null;
            showVerificationMessage('이메일이 변경되었습니다. 인증을 다시 진행해주세요.');
        }
    });

    document.getElementById('joinForm').addEventListener('submit', function (event) {
        if (verifiedEmail !== emailInput.value.trim().toLowerCase()) {
            event.preventDefault();
            showVerificationMessage('이메일 인증을 먼저 완료해주세요.');
        }
    });
});
