import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { MenuItem } from '@mui/material';

const passwordQuestion = [
  {
    question: 'school',
    description: '졸업한 초등학교 이름'
  },
  {
    question: 'school2',
    description: '졸업한 중학교 이름'
  }
];

const SignUpPage = () => {
  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          margin: 5,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center'
        }}
        style={{
          padding: '30px',
          border: '1px solid black',
          borderRadius: '10px',
          backgroundColor: '#ffffff'
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          회원 가입
        </Typography>
        <Box component="form" noValidate sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                // error
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="id"
                label="Id"
                type="id"
                id="id"
                autoComplete="new-id"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password-check"
                label="Password Check"
                type="password"
                id="password-check"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                id="outlined-select-currency"
                select
                label="password-question"
              >
                {passwordQuestion.map((option) => (
                  <MenuItem key={option.question} value={option.description}>
                    {option.description}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="password"
                label="Password Answer"
                type="password"
                id="password"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                fullWidth
                name="phone-number"
                label="Phone Number( - 없이 숫자만 입력)"
                type="tel"
                id="phone-number"
              />
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Sign Up
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="#" variant="body1">
                Already have an account? Sign in
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
};

export default SignUpPage;
