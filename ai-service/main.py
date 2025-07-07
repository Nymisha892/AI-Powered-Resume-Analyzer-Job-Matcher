from fastapi import FastAPI, File, UploadFile
import fitz  # PyMuPDF

app = FastAPI()

@app.post("/analyze-resume")
async def analyze_resume(file: UploadFile = File(...)):
    try:
        content = await file.read()
        with open("temp_resume.pdf", "wb") as f:
            f.write(content)

        doc = fitz.open("temp_resume.pdf")
        text = "\n".join([page.get_text() for page in doc])

        skills = ["Java", "Spring Boot", "MongoDB"]
        role = "Full Stack Developer"

        return {
            "extracted_text": text[:300],  # preview
            "skills": skills,
            "suggested_role": role
        }

    except Exception as e:
        return {"error": str(e)}
