import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import useSwr from 'swr';
import getUser from "../../utils/user";
import {UserStats} from 'types';
import UserStatsItem from '../../components/stats-item';
import OrganizerStatsItem from '../../components/organizer-stats-item';
import Layout from '../../layouts/Main';
import Footer from '../../components/footer';
import { useTranslation } from 'next-i18next'
import { getStaticPaths, makeStaticProps } from '../../utils/get-static'
import Link from '../../components/Link'

const Test = () => {
    const { t } = useTranslation(['common'])
    const router = useRouter()
    const [user, setUser] = useState()

    useEffect(() => {
        const fetchData = async () => {
          const data = await getUser({router})
          console.log(data)
          setUser(data)
        }
      
        fetchData()
          .catch(console.error);
      }, [])

    return (
        <Layout t={t}>
            {user && (
                <p>{user.role}</p>
            )}
        </Layout>
    )
}

export default Test;

const getStaticProps = makeStaticProps(['common'])
export { getStaticPaths, getStaticProps }
