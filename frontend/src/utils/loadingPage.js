import Skeleton from "@mui/material/Skeleton";
import Layout from "../components/layout";

export const LoadingPage = () => {
    return (
        <Layout>
            <Skeleton variant={"rectangular"} width={"100%"} height={"400px"} data-testid="loading"/>
            <Skeleton height={"50px"} width={"60%"}/>
        </Layout>
    )
}