if exist logs\0.nruns del logs\0.nruns
if exist logs\0.jruns del logs\0.jruns
if exist logs\0.graalruns del logs\0.graalruns

FOR %%i IN (0,1,2,3) DO (
  find "time:" logs\nrun%%i >> logs\0.nruns
)

FOR %%i IN (0,1,2,3) DO (
  find "time:" logs\jrun%%i >> logs\0.jruns
)

FOR %%i IN (0,1,2,3) DO (
  find "time:" logs\graalrun%%i >> logs\0.graalruns
)

find "time:" logs\0.nruns >> logs\1.nruns
find "time:" logs\0.jruns >> logs\1.jruns
find "time:" logs\0.graalruns >> logs\1.graalruns

