from fastapi import FastAPI, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
import fitz  # PyMuPDF
import os
import google.generativeai as genai
from dotenv import load_dotenv
import json
import re

# Load environment variables
load_dotenv()
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")

# Configure Gemini
genai.configure(api_key=GOOGLE_API_KEY)
model = genai.GenerativeModel("gemini-1.5-flash")

app = FastAPI()

# Enable CORS (adjust origins as needed)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


def extract_text_from_pdf(file_path: str) -> str:
    doc = fitz.open(file_path)
    text = "\n".join([page.get_text() for page in doc])
    return text


def analyze_resume_with_gemini(resume_text: str) -> dict:
    prompt = f"""
You are a resume parser.

From the following resume text, extract:
1. A list of technical skills
2. The most suitable job role

Respond ONLY in strict JSON format like:
{{
  "skills": ["Java", "Spring Boot", "MongoDB"],
  "role": "Full Stack Developer"
}}

Resume:
\"\"\"
{resume_text}
\"\"\"
"""

    response = model.generate_content(prompt)
    raw_output = response.text.strip()

    # print("raw_output type:", type(raw_output))
    # print("raw-output content:", raw_output)
    # # ✅ Try to parse strict JSON
    print("raw_output type:", type(raw_output))
    print("raw_output content:", repr(raw_output))

    # Step 1: Remove markdown formatting (```json ... ```)
    cleaned = raw_output.strip().strip("`")

    # Step 2: Extract the first valid JSON object using regex
    json_match = re.search(r"\{[\s\S]*?\}", cleaned)
    if not json_match:
        raise ValueError("No valid JSON object found in response.")

    json_str = json_match.group(0)

    print("Cleaned output:", json_str)

    # Step 3: Parse JSON
    try:
        return json.loads(json_str)
    except json.JSONDecodeError as e:
        pass
        print("JSON parsing failed:", e)
        raise ValueError("Failed to parse JSON from model response.")

    # try:
    #     return json.loads(raw_output)
    # except json.JSONDecodeError:
    #     pass  # fallback below

    # 🔍 Fallback regex-based parsing
    skills = []
    role = "Unknown"

    # Try to extract skills from something like: "Skills: Java, Spring Boot, MongoDB"
    # skills_match = re.search(r"(?i)skills?\s*:\s*(.*)", raw_output)
    # if skills_match:
    #     skills_raw = skills_match.group(1)
    #     skills = [s.strip() for s in re.split(r",|\n", skills_raw) if s.strip()]

    # skills_match = re.search(r"(?i)skills?\s*:\s*\[(.*?)\]", raw_output, re.DOTALL)
    # if skills_match:
    #     skills_raw = skills_match.group(1)
    #     # Split on commas, but only outside quotes
    #     skills = [s.strip().strip('"') for s in re.split(r",\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", skills_raw) if
    #               s.strip()]
    # print("type of raw_output", type(raw_output))

    cleaned_output = re.sub(r"^\s*```+\s*json\s*\n", "", raw_output, flags=re.IGNORECASE).strip()
    print(f"Cleaned output: {repr(cleaned_output)[:100]}...")

    # Ensure the output is a valid JSON object by wrapping in braces if needed
    if not cleaned_output.startswith('{'):
        cleaned_output = '{' + cleaned_output
    if not cleaned_output.endswith('}'):
        cleaned_output = cleaned_output + '}'

    try:
        data = json.loads(cleaned_output)
        skills = data.get("skills", [])
    except json.JSONDecodeError as e:
        print(f"Error parsing JSON: {e}")


    print("skills",skills)

    # Try to extract role from something like: "Role: Backend Developer"
    # cleaned_output = re.sub(r"^\s*json\s*\n\s*\{\s*\n", "", raw_output, flags=re.IGNORECASE).strip()
    # print(f"Cleaned output: {repr(cleaned_output)[:100]}...")

    # Try parsing as JSON
    try:
        data = json.loads(cleaned_output)
        role = data.get("role", "")
        print(f"Extracted role from JSON: {role}")
    except json.JSONDecodeError as e:
        role = "Unknown"
        print(f"JSON parsing failed: {e}")


    print("[Gemini Raw Response]", raw_output)


    return {
        "skills": skills,
        "role": role,
        "raw_response": raw_output  # optional: include full text for debugging
    }


@app.post("/analyze-resume")
async def analyze_resume(file: UploadFile = File(...)):
    try:
        contents = await file.read()
        file_path = "temp_resume.pdf"

        with open(file_path, "wb") as f:
            f.write(contents)

        resume_text = extract_text_from_pdf(file_path)

        ai_result = analyze_resume_with_gemini(resume_text)

        return {
            "skills": ai_result.get("skills", []),
            "suggested_role": ai_result.get("role", "Unknown"),
            "extracted_text": resume_text[:300]  # Preview
        }

    except Exception as e:
        return {"error": str(e)}
