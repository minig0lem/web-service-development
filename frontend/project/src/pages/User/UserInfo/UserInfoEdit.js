import Layout from '../../../layouts/Layout';
import TabBar from '../../../components/TabBar/TabBar';
import InnerTabBar from '../../../components/TabBar/InnerTabBar';

const innerTitle = ['개인정보 수정', '대여소 즐겨찾기'];
const outerTab = 'info';
const innerTab = 'edit';
const UserInfoEdit = () => {
  return (
    <Layout>
      <TabBar title={innerTitle} select={outerTab} />
      <InnerTabBar title={innerTitle} select={innerTab} />
      <div>개인정보 수정입니다</div>
    </Layout>
  );
};

export default UserInfoEdit;
