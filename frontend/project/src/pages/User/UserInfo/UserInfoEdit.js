import Layout from '../../../layouts/Layout';
import TabBar from '../../../components/TabBar/TabBar';
import InnerTabBar from '../../../components/TabBar/InnerTabBar';
import { useState } from 'react';
import styles from './UserInfoEdit.module.scss';
import { postFetch } from '../../../config';
import { Grid, Alert, Button } from '@mui/material';
import CheckOutlinedIcon from '@mui/icons-material/CheckOutlined';
import Article from '../../../layouts/Article';

const outerTitle = ['회원정보 관리', '결제 관리', '이용정보 관리'];
const innerTitle = ['개인정보 수정', '대여소 즐겨찾기'];
const outerTab = 'info';
const innerTab = 'edit';
const url = { edit: '/user/info/edit', bookmark: '/user/info/bookmark' };

const UserInfoEdit = () => {
  const [inputData, setInputData] = useState({
    email: 'sibal@naver.com',
    user_id: 'abc123',
    password: '',
    passwordCheck: '',
    phone_number: '01033289942'
  });
  const [isValid, setIsValid] = useState(true);
  const inputDataHandler = (e) => {
    e.preventDefault();
    const key = e.target.id;
    const value = e.target.value;
    setInputData((prevData) => ({ ...prevData, [key]: value }));
  };

  const onSubmitHandler = async (e) => {
    try {
      e.preventDefault();
      console.log(inputData.password.trim());
      if (
        inputData.password.trim() === '' ||
        inputData.passwordCheck.trim() !== inputData.password.trim() ||
        inputData.phone_number.trim() === '' ||
        !inputData.email.includes('@')
      ) {
        setIsValid(false);
      } else {
        setIsValid(true);
        const { status } = await postFetch('url', inputData);
        if (status) {
          alert('개인정보 수정 완료!');
          // navigate('/user/info/edit');
        }
      }
    } catch (error) {
      console.error('개인정보 수정 에러', error);
    }
  };
  return (
    <Layout>
      <TabBar title={outerTitle} select={outerTab} />
      <InnerTabBar title={innerTitle} select={innerTab} url={url} />
      <Article>
        <div className={styles['flex-container']}>
          <table className={styles.table}>
            <colgroup>
              <col width="25%" />
              <col width="75%" />
            </colgroup>
            <tbody>
              <tr>
                <td className={styles.col1}>아이디</td>
                <td className={styles.cell}>{inputData.user_id}</td>
              </tr>
              <tr>
                <td className={styles.col1}>비밀번호</td>
                <td className={styles.cell}>
                  <input
                    className={styles.input}
                    type="password"
                    name="password"
                    id="password"
                    value={inputData.password}
                    onChange={inputDataHandler}
                  />
                  <span style={{ marginLeft: '10px', fontSize: '1rem' }}>
                    <CheckOutlinedIcon
                      sx={{
                        color: 'secondary.light',
                        fontSize: '15px',
                        paddingTop: '4px'
                      }}
                    />
                    20자 이하여야 합니다
                  </span>
                </td>
              </tr>
              <tr>
                <td className={styles.col1}>비밀번호 확인</td>
                <td className={styles.cell}>
                  <input
                    className={styles.input}
                    type="password"
                    name="passwordCheck"
                    id="passwordCheck"
                    value={inputData.passwordCheck}
                    onChange={inputDataHandler}
                  />
                </td>
              </tr>
              <tr>
                <td className={styles.col1}>이메일</td>
                <td className={`${styles.cell} ${styles.tdFlex}`}>
                  <input
                    className={styles.input}
                    type="email"
                    name="email"
                    id="email"
                    value={inputData.email}
                    onChange={inputDataHandler}
                  />
                  <span style={{ marginLeft: '10px', fontSize: '1rem' }}>
                    <CheckOutlinedIcon
                      sx={{
                        color: 'secondary.light',
                        fontSize: '15px',
                        paddingTop: '4px'
                      }}
                    />
                    @를 포함해야 합니다
                  </span>
                </td>
              </tr>
              <tr>
                <td className={styles.col1}>휴대폰 번호</td>
                <td className={styles.cell}>
                  <input
                    className={styles.input}
                    type="tel"
                    name="phone_number"
                    id="phone_number"
                    value={inputData.phone_number}
                    onChange={inputDataHandler}
                  />
                </td>
              </tr>
            </tbody>
          </table>
          {!isValid && (
            <Grid item xs={6} sx={{ color: 'error.main', marginTop: '20px' }}>
              <Alert
                variant="outlined"
                severity="error"
                sx={{ fontSize: '1.3rem' }}
              >
                입력값을 다시 확인해주세요!
              </Alert>
            </Grid>
          )}
          <Button
            onClick={onSubmitHandler}
            type="submit"
            variant="contained"
            sx={{
              mt: 3,
              width: '20vw',
              fontSize: '1.1rem',
              backgroundColor: 'secondary.main',
              '&:hover': {
                backgroundColor: 'secondary.main'
              }
            }}
          >
            수정하기
          </Button>
        </div>
      </Article>
    </Layout>
  );
};
export default UserInfoEdit;
